<%@ Page Title="" Language="C#" MasterPageFile="~/Quality.master" AutoEventWireup="true" CodeFile="QualitySearch.aspx.cs" Inherits="QualitySearch" %>

<asp:Content ID="header" ContentPlaceHolderID="headContent" runat="Server">
    <script src="/Scripts/jquery-2.1.4.min.js"></script>
    <script type="text/javascript">
        function doSearch() {
            var query = document.getElementById('mainContent_tbSearch').value.trim();
            if (query == '') return;
            $.ajax({
                type: "POST",
                url: "QualitySearch.aspx/SearchDocuments",
                data: JSON.stringify({ strQuery: query }),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (response) {
                    var results = response.d;
                    var html = '';
                    if (results.length == 0) {
                        html = '<p style="color:maroon;font-weight:bold;">Aucun fichier trouvé pour cette recherche.</p>';
                    } else {
                        html += '<p><b>' + results.length + ' résultat(s) trouvé(s)</b></p>';
                        for (var i = 0; i < results.length; i++) {
                            var r = results[i];
                            var icon = getIcon(r.FullPath);
                            html += '<div class="divSearchResult">';
                            html += '<img src="' + icon + '" style="width:24px;height:24px;vertical-align:middle;margin-right:8px;" />';
                            html += '<b>' + r.Name + '</b><br/>';
                            html += '<span style="color:gray;font-size:small;">Dossier : ' + r.FolderParent + '</span><br/>';
                            if (r.Description != '')
                                html += '<span style="color:#555;font-size:small;">' + r.Description + '</span><br/>';
                            html += '<span style="color:gray;font-size:small;">Modifié le : ' + r.ModifyDate + '</span>';
                            html += '<br/><a href="javascript:goToFile(\'' + r.FullPath + '\', ' + r.Type + ');" style="font-size:small;">Voir le fichier</a>';
                            html += '</div><hr style="border:0;border-top:1px solid #eee;"/>';
                        }
                    }
                    document.getElementById('divResults').innerHTML = html;
                },
                error: function () {
                    document.getElementById('divResults').innerHTML = '<p style="color:red;">Erreur lors de la recherche.</p>';
                }
            });
        }

        function getIcon(fullPath) {
            var ext = fullPath.split('.').pop().toLowerCase();
            if (ext == 'pdf') return '/Images/icon_pdf.png';
            if (ext == 'doc' || ext == 'docx') return '/Images/icon_word.png';
            if (ext == 'xls' || ext == 'xlsx') return '/Images/icon_excel.png';
            if (ext == 'ppt' || ext == 'pptx') return '/Images/icon_powerpoint.png';
            return '/Images/icon_folder_documents.png';
        }

        function goToFile(strFullPath, nType) {
            // Redirige vers QualityFolder avec le dossier parent
            var parts = strFullPath.split('/');
            parts.pop();
            var folder = parts.join('/');
            window.location.href = 'QualityFolder.aspx?folder=' + encodeURIComponent(folder);
        }

        function onKeyPress(e) {
            if (e.keyCode == 13) doSearch();
        }
    </script>
    <style>
        .divSearchResult {
            padding: 8px;
            margin: 4px 0;
            background: #f9f9f9;
            border-left: 3px solid #0055a4;
        }
        .divSearchResult:hover {
            background: #eef4ff;
        }
    </style>
</asp:Content>

<asp:Content ID="menu" ContentPlaceHolderID="menuContent" runat="Server">
    <ul class="ulMenuVertical">
        <li><a href="QualityFolder.aspx">Retour aux dossiers</a></li>
    </ul>
</asp:Content>

<asp:Content ID="main" ContentPlaceHolderID="mainContent" runat="Server">
    <div class="divLine"></div>
    <h2>Recherche de documents</h2>
    <table style="margin-top:10px; border-spacing:0; border-collapse:collapse;">
        <tr>
            <td>
                <asp:TextBox ID="tbSearch" runat="server" Width="400px" 
                    placeholder="Rechercher un fichier ou dossier..." 
                    onkeypress="onKeyPress(event);" />
            </td>
            <td style="padding-left:8px;">
                <div class="divBlueButton" onclick="doSearch();">Rechercher</div>
            </td>
        </tr>
    </table>
    <div style="margin-top:6px; color:gray; font-size:small;">
        Exemples : « procédure sécurité », « contrat fournisseur », « RH »
    </div>
    <div id="divResults" style="margin-top:20px; max-width:800px;"></div>

    <div class="tdVersion" style="text-align:end; color:black;">
        Version 10_2026
    </div>
</asp:Content>

using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Web.Services;

public partial class QualitySearch : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
    }

    public class SearchResult
    {
        public string Name { get; set; }
        public string FullPath { get; set; }
        public string FolderParent { get; set; }
        public string Description { get; set; }
        public string ModifyDate { get; set; }
        public int Type { get; set; }
    }

    [WebMethod(EnableSession = true)]
    public static List<SearchResult> SearchDocuments(string strQuery)
    {
        List<SearchResult> lstResults = new List<SearchResult>();
        if (string.IsNullOrEmpty(strQuery)) return lstResults;

        // Découper la requête en mots-clés
        string[] mots = strQuery.Split(new char[] { ' ', ',', ';' }, StringSplitOptions.RemoveEmptyEntries);

        // Construire la condition SQL : chaque mot doit apparaître dans le nom OU le dossier OU la description
        string strConditions = "";
        foreach (string mot in mots)
        {
            string m = DBHelper.CharToSQL(mot);
            if (strConditions != "") strConditions += " AND ";
            strConditions += "(BD_NAME LIKE '%" + m + "%' OR BD_FOLDER_PARENT LIKE '%" + m + "%' OR BD_DESCRIPTION LIKE '%" + m + "%')";
        }

        string strSQL = "SELECT BD_FULL_PATH, BD_NAME, BD_FOLDER_PARENT, BD_DESCRIPTION, BD_MODIFY_DATE, BD_TYPE "
                      + "FROM BASE_DOCUMENTAIRES "
                      + "WHERE BD_TYPE = 1 AND " + strConditions
                      + " ORDER BY BD_MODIFY_DATE DESC";

        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        DataTable dt = DBHelper.GetTable(strSQL, strConnection, 200);
        if (dt == null) return lstResults;

        foreach (DataRow row in dt.Rows)
        {
            SearchResult r = new SearchResult();
            r.FullPath = row["BD_FULL_PATH"].ToString();
            r.Name = row["BD_NAME"].ToString();
            r.FolderParent = row["BD_FOLDER_PARENT"].ToString();
            r.Description = row["BD_DESCRIPTION"].ToString();
            r.Type = DBHelper.GetShort(0, "BD_TYPE", dt);
            DateTime dtModify;
            if (DBHelper.GetDate(dt.Rows.IndexOf(row), "BD_MODIFY_DATE", dt, out dtModify))
                r.ModifyDate = dtModify.ToString("dd/MM/yyyy");
            else
                r.ModifyDate = "";
            lstResults.Add(r);
        }
        return lstResults;
    }
}
