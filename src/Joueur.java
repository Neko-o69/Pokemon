function createDiv(strTitle, nType, strFullPath) {
    var div = document.createElement('div');
    div.className = "linkButtonQualityItem";
    div.draggable = true;

    var fct = "onItemClicked( this, " + nType + ", '" + strFullPath + "');";
    div.onclick = new Function(fct);

    var ctxFct = "showPopupMenu(event, this, '" + strFullPath + "'); return false;";
    div.oncontextmenu = new Function(ctxFct);

    // Drag
    div.ondragstart = function (e) {
        e.dataTransfer.setData("text", strFullPath);
        e.dataTransfer.effectAllowed = "move";
    };

    // Drop seulement sur les dossiers
    if (nType == 0) {
        div.ondragover = function (e) {
            e.preventDefault();
            e.stopPropagation();
            e.dataTransfer.dropEffect = "move";
        };

        div.ondrop = function (e) {
            return doDrop(e, strFullPath, nType);
        };
    }

    var table = createTable(strTitle, nType);
    div.appendChild(table);

    return div;
}

[WebMethod(EnableSession = true)]
public static string moveItem(string strSource, string strTarget)
{
    if (HttpContext.Current.Session["User"] == null)
        return "ERROR ! User not login !";

    String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
    String strPath = ConfigurationManager.AppSettings["PATH"];

    String strFileName = strSource.Substring(strSource.LastIndexOf('/') + 1);
    String strNewFullPath = strTarget + "/" + strFileName;

    String strSourcePath = (strPath + strSource.Replace("/", "\\")).Trim();
    String strDestinationPath = (strPath + strNewFullPath.Replace("/", "\\")).Trim();

    try
    {
        if (System.IO.File.Exists(strSourcePath))
        {
            if (System.IO.File.Exists(strDestinationPath))
                return "ERROR : le fichier existe déjà dans ce dossier.";

            System.IO.File.Move(strSourcePath, strDestinationPath);
        }
        else
        {
            return "ERROR : fichier introuvable : " + strSourcePath;
        }

        String strSQL = "UPDATE BASE_DOCUMENTAIRES SET " +
            "BD_FULL_PATH = '" + DBHelper.CharToSQL(strNewFullPath) + "', " +
            "BD_FOLDER_PARENT = '" + DBHelper.CharToSQL(strTarget) + "' " +
            "WHERE BD_FULL_PATH = '" + DBHelper.CharToSQL(strSource) + "'";

        if (DBHelper.SQLExecute(strSQL, strConnection) == false)
            return "ERROR : impossible de mettre à jour la base.";

        return "true";
    }
    catch (Exception ex)
    {
        return "ERROR : " + ex.Message;
    }
}


