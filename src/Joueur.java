if (DBHelper.SQLExecute(strSQL, m_strConnection) == false)
{
    fi.Delete();
    ShowMsgBox("SQL : " + strSQL, "ERROR !", "MsgBox");
    return;
}
