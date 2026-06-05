DataTable dtEmail = DBHelper.SQLOpen("select BD_EMAIL from BD_EMAIL where BD_FULL_PATH = '" + strParentFolder + "'", m_strConnection);
string strTo = "";
if (dtEmail.Rows.Count > 0)
    strTo = dtEmail.Rows[0]["BD_EMAIL"].ToString();
if (String.IsNullOrEmpty(strTo)) strTo = "othmaneqlq@gmail.com"; // email par défaut
