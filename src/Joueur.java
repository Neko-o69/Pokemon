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
