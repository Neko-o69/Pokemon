public static string DeleteDir(string strFolder)
{
    String strRetCode;
    String strSQL;
    int nIndex;
    String strFullPath;
    String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
    try
    {
        DirectoryInfo dir = new DirectoryInfo(strFolder);
        foreach (FileInfo fi in dir.GetFiles())
        {
            // Ok mise à jour dans la database        
            nIndex = fi.FullName.IndexOf("\\Quality");
            strFullPath = fi.FullName.Substring(nIndex + 1);
            strFullPath = strFullPath.Replace('\\', '/');
            strSQL = "delete from  BASE_DOCUMENTAIRES where BD_FULL_PATH = '" + strFullPath + "'";
            if (DBHelper.SQLExecute(strSQL, strConnection) == false)
                return "System error please contact admin for more details !";

            fi.Delete();


        }
        foreach (DirectoryInfo di in dir.GetDirectories())
        {
            strRetCode = DeleteDir(di.FullName);
            if (strRetCode != "true") return strRetCode;
        }

        // Finalement
        dir.Delete();

    }
    catch (Exception ex)
    {
        return "ERROR : " + ex.Message;
    }

    // Ok mise à jour dans la database
    nIndex = strFolder.IndexOf("\\Quality");
    strFullPath = strFolder.Substring(nIndex + 1);
    strFullPath = strFullPath.Replace('\\', '/');
    strSQL = "delete from  BASE_DOCUMENTAIRES where BD_FULL_PATH = '" + strFullPath + "'";
    if (DBHelper.SQLExecute(strSQL, strConnection) == false)
        return "System error please contact admin for more details !";

    return "true";
}

[WebMethod(EnableSession = true)]
public static string deleteFolder(string strFullPath)
{
    String strPath = HttpContext.Current.Server.MapPath("Images\\") + strFullPath;
    String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
    if (Directory.Exists(strPath))
    {
        DBHelper.SQLExecute("delete from BASE_DOCUMENTAIRES where BD_FULL_PATH LIKE '" + strFullPath + "'", strConnection);
        return "true";
    }

        return DeleteDir(strPath);
}
