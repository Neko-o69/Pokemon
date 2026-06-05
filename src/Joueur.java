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
        DataTable dtEmail = DBHelper.SQLOpen("select BD_EMAIL from BD_EMAIL where BD_FULL_PATH = '" + strParentFolder + "'", m_strConnection);
        string strTo = "";
        if (dtEmail.Rows.Count > 0)
            strTo = dtEmail.Rows[0]["BD_EMAIL"].ToString();
        if (String.IsNullOrEmpty(strTo)) strTo = "othmaneqlq@gmail.com"; // email par défaut
        string result = sendRequestModification(strTo, strObject, strBody, strFullPath);
        if (result != "true")
            ShowMsgBox(result, "ERREUR MAIL", "MsgBox");



        // Reload...
        //gotoFolder(strParentFolder);
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
