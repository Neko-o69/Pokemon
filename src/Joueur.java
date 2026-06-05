[WebMethod(EnableSession = true)]
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
    service.Credentials = new WebCredentials(strUser, strPW, "NIDEK");
    service.Url = new Uri( "https://mail.nidek.fr/EWS/Exchange.asmx" );
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
        message.Send();
    }
    catch (Exception ex)
    {
        return "ERROR : " + ex.ToString();
    }

    return "true";
}
