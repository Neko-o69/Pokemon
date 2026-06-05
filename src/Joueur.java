<%@ Page Title="" Language="C#" MasterPageFile="~/Quality.master" AutoEventWireup="true" CodeFile="QualityProcess.aspx.cs" Inherits="QualityProcess" %>

<asp:Content ID="head" ContentPlaceHolderID="headContent" runat="Server">
</asp:Content>
<asp:Content ID="menu" ContentPlaceHolderID="menuContent" runat="Server">
</asp:Content>
<asp:Content ID="main" ContentPlaceHolderID="mainContent" runat="Server">

    <script src="Scripts/utility.js"></script>
    <script src="Scripts/Quality.js"></script>
    <script type="text/javascript">

        function onItemClicked(strSubFolder) {
            var hf = document.getElementById('mainContent_hfProcess');
            if (hf == null) return true;
            var strFullPath = 'Quality/' + hf.value + '/' + strSubFolder;
            window.location.href = 'QualityFolder.aspx?folder=' + strFullPath;
            return false;
        }

    </script>

    <asp:HiddenField ID="hfProcess" Value="AUT" runat="server" />

    <%--Title --%>
    <asp:Label ID="lbTitle" runat="server" CssClass="labelTitle" Text="Processus ???" Style="margin-left: 10px;"></asp:Label>
    <div class="divLine"></div>

    <table style="margin-top: 10px; margin-left: 250px; border-spacing: 0; border-collapse: collapse;">
        <tr>
            <td style="text-align: center;">
                <a href="#">
                    <asp:LinkButton ID="btnRef" runat="server" CssClass="divQualityPyramid" Style="background-image: url('../Images/icon_pyramid_1.png'); padding-top: 20px;"
                        OnClientClick="return onItemClicked('REF');"
                        Width="128px" Height="65px">
                        Document de<br />référence-<br />REF</asp:LinkButton>
                </a>
            </td>
        </tr>
        <tr>
            <td style="text-align: center;">
                <asp:LinkButton ID="btnProc" runat="server" CssClass="divQualityPyramid" Style="background-image: url('../Images/icon_pyramid_2.png'); padding-top: 20px;"
                    OnClientClick="return onItemClicked('PROC');" Width="244px" Height="65px">
                    Procédure - <br />PROC</asp:LinkButton>
            </td>
        </tr>
        <tr>
            <td style="text-align: center;">
                <asp:LinkButton ID="btnMOP" runat="server" CssClass="divQualityPyramid" Style="background-image: url('../Images/icon_pyramid_3.png'); padding-top: 30px;"
                    OnClientClick="return onItemClicked('MOP');" Width="364px" Height="55px">
                    Mode opératoire - MOP</asp:LinkButton>

            </td>
        </tr>
        <tr>
            <td style="text-align: center;">
                <asp:LinkButton ID="btnFORM" runat="server" CssClass="divQualityPyramid" Style="background-image: url('../Images/icon_pyramid_4.png'); padding-top: 30px;"
                    OnClientClick="return onItemClicked('FORM');" Width="484px" Height="55px">
                    Formulaires - FORM</asp:LinkButton>
            </td>
        </tr>
        <tr>
            <td style="text-align: center;">
                <asp:LinkButton ID="btnDOC" runat="server" CssClass="divQualityPyramid" Style="background-image: url('../Images/icon_pyramid_5.png'); padding-top: 30px;"
                    OnClientClick="return onItemClicked('DOC');" Width="604px" Height="160px">
                    Documents - DOC</asp:LinkButton>
            </td>
            <td style="text-align:center;">
                <%if (Request.QueryString["Type"] == "DRH")
                    { %>
                <asp:LinkButton ID="BtnACC" runat="server" CssClass="divQualityPyramid" Style="background-image: url('../Images/icon_pyramid_rond.png'); padding-top: 80px; text-align:center;  background-repeat: no-repeat; line-height:18px; background-size:200px 200px;"
                    OnClientClick="return onItemClicked('ACC');" Width="200px" Height="160px">
                    Accords d'entreprise<br />
                    Chartes
                </asp:LinkButton>
                <% }  %>
            </td>
        </tr>

        <%--<tr>
            <td style="text-align:center;">
                <asp:LinkButton ID="LinkButton1" runat="server" CssClass="divQualityPyramid" style="background-image:url('../Images/icon_pyramid_rond2.png');padding-top:30px;background-size:contain;"
                    OnClientClick="return onItemClicked('DOC');" Width="120px" Height="55px">
                    Documents - DOC</asp:LinkButton>
            </td>
        </tr>--%>
    </table>





</asp:Content>

