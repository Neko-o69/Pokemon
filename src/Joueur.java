[WebMethod(EnableSession = true)]
public static string moveItem(string strSource, string strTarget)
{
    if (HttpContext.Current.Session["User"] == null)
        return "ERROR ! User not login !";

    try
    {
        if (String.IsNullOrEmpty(strSource))
            return "ERROR : fichier source vide";

        if (String.IsNullOrEmpty(strTarget))
            return "ERROR : dossier destination vide";

        String strConnection =
            ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;

        String strFileName =
            strSource.Substring(strSource.LastIndexOf('/') + 1);

        String strNewFullPath =
            strTarget.TrimEnd('/') + "/" + strFileName;

        // Chemin du fichier actuel
        String strSourcePath =
            HttpContext.Current.Server.MapPath("~/Images/") +
            strSource.Replace("/", "\\");

        // Chemin du nouveau fichier
        String strDestinationPath =
            HttpContext.Current.Server.MapPath("~/Images/") +
            strNewFullPath.Replace("/", "\\");

        // Vérifie que le fichier existe
        if (System.IO.File.Exists(strSourcePath) == false)
            return "ERROR : fichier introuvable : " + strSourcePath;

        // Vérifie que le dossier destination existe
        String strDestinationFolder =
            System.IO.Path.GetDirectoryName(strDestinationPath);

        if (System.IO.Directory.Exists(strDestinationFolder) == false)
            return "ERROR : dossier destination introuvable";

        // Vérifie qu'un fichier du même nom n'existe pas déjà
        if (System.IO.File.Exists(strDestinationPath))
            return "ERROR : le fichier existe déjà dans ce dossier";

        // Déplacement du fichier
        // File.Move supprime automatiquement le fichier de l'ancien emplacement
        System.IO.File.Move(strSourcePath, strDestinationPath);

        // Mise à jour de la base
        String strSQL =
            "UPDATE BASE_DOCUMENTAIRES SET " +
            "BD_FULL_PATH = '" + DBHelper.CharToSQL(strNewFullPath) + "', " +
            "BD_FOLDER_PARENT = '" + DBHelper.CharToSQL(strTarget) + "' " +
            "WHERE BD_FULL_PATH = '" + DBHelper.CharToSQL(strSource) + "'";

        if (DBHelper.SQLExecute(strSQL, strConnection) == false)
        {
            // Si la base ne se met pas à jour,
            // on remet le fichier à son emplacement d'origine
            if (System.IO.File.Exists(strDestinationPath))
            {
                System.IO.File.Move(strDestinationPath, strSourcePath);
            }

            return "ERROR : impossible de modifier la base de données";
        }

        return "true";
    }
    catch (Exception ex)
    {
        return "ERROR : " + ex.Message;
    }
}
