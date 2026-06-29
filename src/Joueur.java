[WebMethod(EnableSession = true)]
public static string requestSaveProperties(string strData)
{
    String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
    List<QualityItem> lstItem = JsonConvert.DeserializeObject<List<QualityItem>>(strData);
    String strName = lstItem[0].strName.Replace("&quote", "'");
    String strDescription = lstItem[0].strDescription.Replace("&quote", "'");

    String strSQL = "update BASE_DOCUMENTAIRES set BD_NAME = '" + DBHelper.CharToSQL(strName)
        + "', BD_DESCRIPTION = '" + DBHelper.CharToSQL(strDescription) + "' where BD_FULL_PATH = '" + lstItem[0].strFullPath + "'";
    if (DBHelper.SQLExecute(strSQL, strConnection) == false) return "false";

    NKUser nkUser = (NKUser)HttpContext.Current.Session["User"];
    if (nkUser.HasRight(NKUser.USER_RIGHT_QUALITY))
    {
        strSQL = "update BD_EMAIL set BD_EMAIL.BD_EMAIL = '" + lstItem[0].strEMail
                    + "' where BD_FULL_PATH = '" + lstItem[0].strFullPath + "'";
        if (DBHelper.SQLExecute(strSQL, strConnection) == false) return "false";
    }
    return "true";
}
