String strSQL = "update BASE_DOCUMENTAIRES set BD_NAME = '" + DBHelper.CharToSQL(strName)
    + "', BD_DESCRIPTION = '" + DBHelper.CharToSQL(strDescription) + "' where BD_FULL_PATH = '" + lstItem[0].strFullPath + "'";

if (DBHelper.SQLExecute(strSQL, strConnection) == false) return "false";

strSQL = "UPDATE BASE_DOCUMENTAIRES "
    + "SET BD_FULL_PATH = REPLACE(BD_FULL_PATH, '" + lstItem[0].strFullPath + "', '" + lstItem[0].strParent + "/" + lstItem[0].strName + "') "
    + "where BD_FULL_PATH like '" + lstItem[0].strFullPath + "'";

if (DBHelper.SQLExecute(strSQL, strConnection) == false) return "false";

strSQL = "UPDATE BASE_DOCUMENTAIRES "
    + "SET BD_FOLDER_PARENT = REPLACE(BD_FOLDER_PARENT, '" + lstItem[0].strFullPath + "', '" + lstItem[0].strParent + "/" + lstItem[0].strName + "') "
    + "where BD_FULL_PATH like '" + lstItem[0].strParent + "/" + lstItem[0].strName + "'";

if (DBHelper.SQLExecute(strSQL, strConnection) == false) return "false";
