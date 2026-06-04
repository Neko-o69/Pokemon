// Update BD_NAME seulement
strSQL = "UPDATE BASE_DOCUMENTAIRES SET BD_NAME = '" + DBHelper.CharToSQL(strName) + "'" +
    ", BD_DESCRIPTION = '" + DBHelper.CharToSQL(strDescription) + "'" +
    " WHERE BD_FULL_PATH = '" + lstItem[0].strFullPath + "'";

if (DBHelper.SQLExecute(strSQL, strConnection) == false) return "false";
