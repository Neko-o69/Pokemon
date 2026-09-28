if (DBHelper.SQLExecute(strSQL, m_strConnection) == false)
{
    fi.Delete();
    ShowMsgBox("System error please contact admin for more details !", "ERROR !", "MsgBox");
    return;
}


// Ouverture du mail
string destinataire = "Eric_Nara@nidek.fr";

string sujet = "Nouveau document ajouté";

string corps = "Un nouveau document a été ajouté."
             + "\r\n\r\n"
             + "Dossier : " + strParentFolder
             + "\r\n"
             + "Fichier : " + strDisplay
             + "\r\n"
             + "Ajouté par : " + nkUser.m_strCode;

string mailto = "mailto:" + destinataire
              + "?subject=" + HttpUtility.UrlEncode(sujet)
              + "&body=" + HttpUtility.UrlEncode(corps);

ClientScript.RegisterStartupScript(
    GetType(),
    "openMail",
    "window.location.href='" + mailto.Replace("'", "\\'") + "';",
    true
);


// Reload...
gotoFolder(strParentFolder);
