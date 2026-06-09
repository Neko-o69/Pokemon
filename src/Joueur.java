public static string requestSaveProperties(string strData)
{
    String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
    List<QualityItem> lstItem = JsonConvert.DeserializeObject<List<QualityItem>>(strData);

    String strName = lstItem[0].strName.Replace("&quote", "'");
    String strDescription = lstItem[0].strDescription.Replace("&quote", "'");
    String strExtension = Path.GetExtension(lstItem[0].strFullPath);


    String strSQL = "update BASE_DOCUMENTAIRES set BD_NAME = '" + DBHelper.CharToSQL(strName)
        + "', BD_DESCRIPTION = '" + DBHelper.CharToSQL(strDescription) + "' where BD_FULL_PATH = '" + lstItem[0].strFullPath + "'";


    if (DBHelper.SQLExecute(strSQL, strConnection) == false) return "false";


    strSQL = "UPDATE BASE_DOCUMENTAIRES "
 + "SET BD_FULL_PATH = REPLACE(BD_FULL_PATH, '" + lstItem[0].strFullPath + "', '" + lstItem[0].strParent + "/" + lstItem[0].strName + strExtension +"') "
 + "where BD_FULL_PATH like '" + lstItem[0].strFullPath + "'";

    if (DBHelper.SQLExecute(strSQL, strConnection) == false) return "false";

    strSQL = "UPDATE BASE_DOCUMENTAIRES "
+ "SET BD_FOLDER_PARENT = REPLACE(BD_FOLDER_PARENT, '" + lstItem[0].strFullPath + "', '" + lstItem[0].strParent + "/" + lstItem[0].strName + strExtension + "') "
+ "where BD_FULL_PATH like '" + lstItem[0].strParent + "/" + lstItem[0].strName + strExtension + "'";

    if (DBHelper.SQLExecute(strSQL, strConnection) == false) return "false";




    String strPath = ConfigurationManager.AppSettings["PATH"];

    //string strSource = "C:/inetpub/wwwroot/Quality/Images/" + lstItem[0].strFullPath;
    //string strDestination = "C:/inetpub/wwwroot/Quality/Images/" + lstItem[0].strParent + "/" + lstItem[0].strName;
    
    string strSource = strPath + lstItem[0].strFullPath;
    string strDestination = strPath + lstItem[0].strParent + '/' + lstItem[0].strName + strExtension;
    
   
    try
    {
        if (File.Exists(strSource))
            File.Move(strSource, strDestination);
    }
    catch (Exception e)
    {
        return "false : " + e.Message;
    }
    // Update aussi l'email de notification ssi droit Ok
    NKUser nkUser = (NKUser)HttpContext.Current.Session["User"];
    if (nkUser.HasRight(NKUser.USER_RIGHT_QUALITY))
    {
        strSQL = "update BD_EMAIL set BD_EMAIL.BD_EMAIL = '" + lstItem[0].strEMail
                    + "' where BD_FULL_PATH = '" + lstItem[0].strFullPath + "'";

       if (DBHelper.SQLExecute(strSQL, strConnection) == false) return "false";
    }


    return "true";
}
