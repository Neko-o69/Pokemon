if (uploadedCount > 0)
{
    string strResponsable = GetResponsableEmail(strParentFolder);

    if (!String.IsNullOrWhiteSpace(strResponsable))
    {
        string strListeDocuments = "";
        foreach (string document in uploadedDocuments)
        {
            strListeDocuments += "- " + document + "\r\n";
        }

        string strSujet = "Nouveau document ajouté";

        string strCorps =
            "Bonjour,\r\n\r\n" +
            "Un ou plusieurs documents ont été ajoutés.\r\n\r\n" +
            "Document(s) :\r\n" +
            strListeDocuments +
            "\r\n" +
            "Dossier : " + strParentFolder + "\r\n" +
            "Ajouté par : " + nkUser.m_strCode + "\r\n\r\n" +
            "Cordialement";

        string mailto =
            "mailto:" + strResponsable +
            "?subject=" + Uri.EscapeDataString(strSujet) +
            "&body=" + Uri.EscapeDataString(strCorps);

        string script =
            "window.open('" +
            HttpUtility.JavaScriptStringEncode(mailto) +
            "', '_blank');";

        // Clé unique = dossier + liste des fichiers uploadés
        string strMailKey = strParentFolder + "|" + strListeDocuments;
        string strSessionKey = "dernierMail_" + strParentFolder;

        string strDernierMail = Session[strSessionKey] as string;
        if (strDernierMail != strMailKey)
        {
            Session[strSessionKey] = strMailKey;
            ScriptManager.RegisterStartupScript(
                this,
                GetType(),
                "OpenOutlookDraft",
                script,
                true
            );
        }
    }
}
