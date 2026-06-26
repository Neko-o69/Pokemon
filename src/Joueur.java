[WebMethod(EnableSession = true)]
public static string sendRequestModification(string strTo, string strObject, string strBody, string strAttach)
{
    if (HttpContext.Current.Session["User"] == null) return "ERROR ! User not login !";

    NKUser nkUser = (NKUser)HttpContext.Current.Session["User"];
    String strXmlFile = HttpContext.Current.Server.MapPath("App_Data") + "\\Users\\" + nkUser.m_strCode + "\\SmtpServer.xml";
    if (NKTools.IsFileExist(strXmlFile) == false) return "ERROR ! Configuration file for email not found ";

    XmlDocument xmlFile = new XmlDocument();
    xmlFile.Load(strXmlFile);
    String strUser = xmlFile.GetElementsByTagName("User")[0].InnerText;
    String strPw = xmlFile.GetElementsByTagName("MW")[0].InnerText;

    try
    {
        SmtpClient client = new SmtpClient("smtp.office365.com", 587);
        client.EnableSsl = true;
        client.Credentials = new NetworkCredential(strUser, strPw);

        MailMessage mail = new MailMessage();
        mail.From = new MailAddress(strUser);
        mail.To.Add(strTo);
        mail.Subject = strObject;
        mail.Body = strBody.Replace("\r\n", "<br/>");
        mail.IsBodyHtml = true;

        if (String.IsNullOrEmpty(strAttach) == false)
        {
            String strAttachFile = HttpContext.Current.Server.MapPath("Images\\") + strAttach;
            if (NKTools.IsFileExist(strAttachFile))
                mail.Attachments.Add(new Attachment(strAttachFile));
        }

        client.Send(mail);
        return "true";
    }
    catch (Exception ex)
    {
        return "ERROR : " + ex.Message;
    }
}
