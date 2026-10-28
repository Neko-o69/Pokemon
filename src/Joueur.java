[WebMethod(EnableSession = true)]
public static string requestSaveProperties(string strData)
{
    String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
    List<QualityItem> lstItem = JsonConvert.DeserializeObject<List<QualityItem>>(strData);
    String strName = lstItem[0].strName.Replace("&quote", "'");
    String strDescription = lstItem[0].strDescription.Replace("&quote", "'");
    String strOldFullPath = lstItem[0].StrFullPath;

    // Construire le nouveau chemin (même dossier, nouveau nom)
    String strFolder = strOldFullPath.Substring(0, strOldFullPath.LastIndexOf('\\') + 1);
    String strExtension = System.IO.Path.GetExtension(strOldFullPath);
    String strNewFullPath = strFolder + strName + strExtension;

    // Renommer le fichier physique sur disque
    if (strNewFullPath != strOldFullPath)
    {
        if (!System.IO.File.Exists(strOldFullPath)) return "Fichier introuvable sur le serveur.";
        if (System.IO.File.Exists(strNewFullPath)) return "Un fichier avec ce nom existe déjà.";
        System.IO.File.Move(strOldFullPath, strNewFullPath);
    }

    // Mettre à jour le nom, la description ET le chemin en DB
    String strSQL = "update BASE_DOCUMENTAIRES set BD_NAME = '" + DBHelper.CharToSQL(strName)
        + "', BD_DESCRIPTION = '" + DBHelper.CharToSQL(strDescription)
        + "', BD_FULL_PATH = '" + DBHelper.CharToSQL(strNewFullPath)
        + "' where BD_FULL_PATH = '" + DBHelper.CharToSQL(strOldFullPath) + "'";
    if (DBHelper.SQLExecute(strSQL, strConnection) == false) return "false";

    NKUser nkUser = (NKUser)HttpContext.Current.Session["User"];
    if (nkUser.HasRight(NKUser.USER_RIGHT_QUALITY))
    {
        strSQL = "update BD_EMAIL set BD_EMAIL.BD_EMAIL = '" + lstItem[0].strEMail
                    + "' where BD_FULL_PATH = '" + DBHelper.CharToSQL(strNewFullPath) + "'";
        if (DBHelper.SQLExecute(strSQL, strConnection) == false) return "false";
    }
    return "true";
}
