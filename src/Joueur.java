<%@ Master Language="C#" AutoEventWireup="true" CodeFile="Quality.master.cs" Inherits="Quality" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, user-scalable=no" />

    <link href="Content/Quality.css" rel="Stylesheet" type="text/css" />
    <%--For Computer device--%>
    <link rel="icon" href="ipad_nidek_interne.png" />
    <link rel="shortcut icon" href="icon_nidek_interne.ico" />
    <%--For mobile device--%>
    <link rel="apple-touch-icon" href="ipad_nidek_interne.png" />
    <link rel="apple-touch-icon-precomposed" href="icon_nidek_interne.png" />
    <%--Indicate whether to hide the browser's status bar on startup:--%>
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <%--Indicate an image to display while the mobile web app is loading:--%>
    <link rel="apple-touch-startup-image" href="ipad_nidek_interne.png" />

    <script type="text/javascript" src="Scripts/QualityMaster.js"></script>

    <title>NIDEK - Base documentaire</title>

    <asp:ContentPlaceHolder ID="headContent" runat="server">
    </asp:ContentPlaceHolder>

</head>
<body onload="hideMenu()">
    <form id="mainForm" runat="server">

        <script type="text/javascript">

            function hideMenu() {
                var strCurrentPage = "";
                var nIndex = window.location.href.lastIndexOf('/');
                if (nIndex > 0)
                    strCurrentPage = window.location.href.substring(nIndex + 1);
                if (strCurrentPage == 'QualityLogin.aspx') {
                    var tbl = document.getElementById('tableHeader');
                    if (tbl)
                        tbl.style.display = 'none';
                }

            }

        </script>

        <asp:ScriptManager ID="ajaxMainScript" runat="server" EnableScriptGlobalization="true" EnableScriptLocalization="true"></asp:ScriptManager>


        <table>
            <%--Le Header--%>
            <tr>
                <td></td>
                <td>
                    <div style="min-width: 1024px;">
                        <table id="tableHeader" style="border-spacing: 0; border-collapse: collapse;">
                            <tr>
                                <td>
                                    <asp:TextBox ID="tbMasterSearch" CssClass="tbSearch" Style="margin-left: 9px;"
                                        runat="server" placeholder="Recherche.." AutoPostBack="false" onkeydown="return onSearchKeyDown(event);"></asp:TextBox>
                                </td>
                                <td>
                                    <h1 style="margin-left: 5px; margin-right: 5px;">Base documentaire NIDEK</h1>
                                </td>
                                <td>
                                    <ul class="ulMenuHeaderBlue">
                                        <li><a href="javascript:onHome();">Accueil</a></li>
                                        <li><a href="QualityLogin.aspx">Se déconnecter</a></li>
                                        <%--<li><a href="javascript:onChangePassword();">Changer de mot de passe</a></li>--%>
                                    </ul>
                                </td>
                                <td>
                                    <asp:Label ID="lbUserName" runat="server" Text="Utilisateur" CssClass="labelMenuHeaderBlue"></asp:Label>
                                </td>
                            </tr>
                        </table>
                    </div>
                </td>
            </tr>
            <tr>
                <%--Le menu vertical coté gauche--%>
                <td style="vertical-align: top; min-width: 130px;">
                    <asp:ContentPlaceHolder ID="menuContent" runat="server">
                    </asp:ContentPlaceHolder>
                </td>
                <%--Le contenu--%>
                <td style="width: 100%; vertical-align: top;">
                    <div id="divBody" class="divMainContent">
                        <asp:ContentPlaceHolder ID="mainContent" runat="server">
                        </asp:ContentPlaceHolder>
                    </div>
                </td>
            </tr>

        </table>


    </form>
</body>
</html>
