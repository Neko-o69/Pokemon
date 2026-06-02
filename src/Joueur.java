protected void UploadFile()
{

    NKUser nkUser = (NKUser)Session["User"];
    if (nkUser.HasRight(NKUser.USER_RIGHT_UPLOAD) == false)
    {
        ShowMsgBox("Vous n'avez pas le droit pour cette opération", "ERREUR !", "MsgBox");
        return;
    }
    if (DocFileUpload.HasFile == false) return;

    // Save this file to temps folder
    String strDate = DBHelper.GetCurrentDate();
    String strParentFolder = hfFolderTree.Value;
    if (String.IsNullOrEmpty(strParentFolder))
        strParentFolder = "Quality";
    String strFullPath = strParentFolder + "/" + DocFileUpload.FileName.Replace("'", "&quote");
    String strFileName = Server.MapPath("Images/") + strFullPath;

    int nIndex = DocFileUpload.FileName.LastIndexOf('.');
    if (nIndex < 0) return;
    String strExt = DocFileUpload.FileName.Substring(nIndex + 1).ToUpper();
    String strDisplay = tbDisplay.Text;
    if (strDisplay == "")
        strDisplay = DocFileUpload.FileName.Substring(0, nIndex);
    String strDescription = tbDescription.Text;

    switch (strExt)
    {
        case "DOC":
        case "DOCX":
            nIndex = 1;
            break;

        case "XLS":
        case "XLSX":
            nIndex = 2;
            break;

        case "PDF":
            nIndex = 3;
            break;

        case "PNG":
        case "JPG":
        case "JPEG":
        case "GIF":
        case "BMP":
            nIndex = 4;
            break;

        case "PPT":
        case "PPTX":
            nIndex = 5;
            break;

        case "ZIP":
        case "RAR":
            nIndex = 6;
            break;

        case "MPP":
            nIndex = 7;
            break;

        case "XML":
            nIndex = 8;
            break;
        case "BIN":
            nIndex = 9;
            break;
        case "DWG":
            nIndex = 10;
            break;

        default:
            ShowMsgBox("Le type de fichier [" + strExt + "] n'est pas connu !\r\nLes fichiers valables sont Word, Pdf, Excel, Images, Zip.", "ERREUR !", "MsgBox");
            return;

    }

    try
    {
        FileInfo fi = new FileInfo(strFileName);
        if (fi.Exists)
        {
            fi.Delete();
        }
        DocFileUpload.SaveAs(strFileName);

        // Save ok, insert into database
        String strParent = strParentFolder;
        //if (strParent == "Quality") strParent = ""; // Suis à la racine !
        int nOrder = 0;
        DataTable dt;
        if (strParent == "")
            dt = DBHelper.SQLOpen("select BD_ORDER from BASE_DOCUMENTAIRES " +
                "where BD_FOLDER_PARENT is NULL or BD_FOLDER_PARENT = '' or BD_FOLDER_PARENT = 'Quality' order by BD_ORDER desc", m_strConnection);
        else
            dt = DBHelper.SQLOpen("select BD_ORDER from BASE_DOCUMENTAIRES where BD_FOLDER_PARENT = '" + strParent + "' order by BD_ORDER desc", m_strConnection);

        if (dt.Rows.Count > 0)
            nOrder = DBHelper.GetInt(0, "BD_ORDER", dt) + 1;


        String strSQL = "insert into BASE_DOCUMENTAIRES" +
            "(BD_FULL_PATH, BD_NAME, BD_DESCRIPTION, BD_FOLDER_PARENT, BD_CREATE_DATE, BD_CREATE_USER, BD_MODIFY_DATE, BD_MODIFY_USER, BD_TYPE, BD_ORDER) values (" +
            "'" + DBHelper.CharToSQL(strFullPath) + "', '" + DBHelper.CharToSQL(strDisplay) + "', '" + DBHelper.CharToSQL(strDescription) + "', '" + strParent + "', " +
            strDate + ", '" + nkUser.m_strCode + "', NULL, NULL, " + nIndex.ToString() + ", " + nOrder.ToString() + ")";
        if (DBHelper.SQLExecute(strSQL, m_strConnection) == false)
        {
            fi.Delete();
            ShowMsgBox("System error please contact admin for more details !", "ERROR !", "MsgBox");
            return;
        }
        string destinataire = "Eric_Nara@nidek.fr";
        sendRequestModification(destinataire, "un nouveau document a été ajouté" + "Nom : " + strDisplay + "Ajouté par : " + nkUser.m_strCode, strFullPath);
     

        // Reload...
        gotoFolder(strParentFolder);
        //if(dlFolder.Items.Count <= 0)
        //{
        //    hfAddNewFile.Value = "";
        //    gotoFolder(strParentFolder);
        //}
        //else
        //    hfAddNewFile.Value = strDisplay + "/" + nIndex.ToString() + "/" + strFullPath;
    }
    catch (Exception ex)
    {
        ShowMsgBox(ex.Message, "ERREUR SYSTEME !", "MsgBox");
    }

}



public static string sendRequestModification(string strTo, string strObject, string strBody, string strAttach)
{
    String strUser, strPW, strServer, strAuthenfification, strXmlFile, strAttachFile, strEMail;
    XmlDocument xmlFile = new XmlDocument();
    XmlNodeList xlmItems;

    if (HttpContext.Current.Session["User"] == null) return "ERROR ! User not login !";
    NKUser nkUser = (NKUser)HttpContext.Current.Session["User"];
    strXmlFile = HttpContext.Current.Server.MapPath("App_Data") + "\\Users\\" + nkUser.m_strCode + "\\SmtpServer.xml";
    if (NKTools.IsFileExist(strXmlFile) == false) return "ERROR ! Configuration file for email not found ";
    xmlFile.Load(strXmlFile);

    xlmItems = xmlFile.GetElementsByTagName("Authentification");
    strAuthenfification = xlmItems[0].InnerText.ToString().ToUpper();

    xlmItems = xmlFile.GetElementsByTagName("User");
    strUser = xlmItems[0].InnerText.ToString();

    xlmItems = xmlFile.GetElementsByTagName("MW");
    strPW = xlmItems[0].InnerText.ToString();

    xlmItems = xmlFile.GetElementsByTagName("SMTP");
    strServer = xlmItems[0].InnerText.ToString();


    // Reformatter...
    String strText = strBody.Replace('|', '\'');


    ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
    service.Credentials = new WebCredentials(strUser, strPW);
    //service.Url = new Uri( "https://mail.nidek.fr/EWS/Exchange.asmx" );
    service.Url = new Uri(strServer);
    EmailMessage message = new EmailMessage(service);
    message.Subject = strObject;
    //message.Body = MessageTextBox.Text;
    message.Body = strText.Replace("\r\n", "<br/>");

    //|*******************************************************************
    //|-- Multi destiny
    int nOffset = 0;
    int nIndex = strTo.IndexOf(';');
    while (nIndex > 0)
    {
        strEMail = strTo.Substring(nOffset, nIndex - nOffset).Trim();
        if (String.IsNullOrEmpty(strEMail) == false)
            message.ToRecipients.Add(strEMail);

        nOffset += (nIndex - nOffset) + 1;
        nIndex = strTo.IndexOf(';', nOffset);
    }
    // Last item;
    strEMail = strTo.Substring(nOffset).Trim();
    if (String.IsNullOrEmpty(strEMail) == false)
        message.ToRecipients.Add(strEMail);
    //|*******************************************************************

    // Cc tjs à audrey
    String strCC = ConfigurationManager.AppSettings["QualityManager"];
    if (String.IsNullOrEmpty(strCC) == false)
    {
        message.CcRecipients.Add(strCC);
    }

    // Attachment
    strAttachFile = HttpContext.Current.Server.MapPath("Images\\") + strAttach;
    strAttachFile = strAttachFile.Replace("/", "\\");
    if (NKTools.IsFileExist(strAttachFile) == false)
        return "ERROR ! Attach file not exist";

    message.Attachments.AddFileAttachment(strAttachFile);
    try
    {
        message.Save();
        message.SendAndSaveCopy();
    }
    catch (Exception ex)
    {
        return "ERROR : " + ex.ToString();
    }

    return "true";
}
