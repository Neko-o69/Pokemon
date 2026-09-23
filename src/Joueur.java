protected void gotoFolder(String strFullPath)
{
    String strSQL = "";

    if (strFullPath == "")
        strSQL = "select * from BASE_DOCUMENTAIRES where BD_FOLDER_PARENT is NULL or BD_FOLDER_PARENT = '' or BD_FOLDER_PARENT = 'Quality' order by BD_TYPE, BD_ORDER";
    else
        strSQL = "select * from BASE_DOCUMENTAIRES where BD_FOLDER_PARENT = '" + strFullPath + "' order by BD_TYPE, BD_NAME ASC";

    hfFolderTree.Value = strFullPath;
    // Extract le dossier courant
    String strPath = strFullPath;
    int nIndex = strPath.LastIndexOf('/');
    if (nIndex >= 0)
        strPath = strPath.Substring(nIndex + 1);
    hfCurrentFolder.Value = strPath;

    DataTable dt = DBHelper.SQLOpen(strSQL, m_strConnection);
    dlFolder.DataSource = dt;
    dlFolder.DataBind();
    Session["BASE_DOCUMENTAIRES"] = dt;

}
