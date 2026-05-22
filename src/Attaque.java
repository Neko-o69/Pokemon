public class Attaque {
    private String nom;
    private int puissance;
    private Type type;

    public Attaque(String nom, int puissance, Type type) {
        this.nom = nom;
        this.puissance = puissance;
        this.type = type;
    }

    public int getPuissance() {
        return puissance;
    }

    public Type getType() {
        return type;
    }

    public String getNom() {
        return nom;
    }
}



<%-- Panel pour la gestion des mails de demande de modification --%>
<asp:Panel ID="panelMailsModif" runat="server" Width="450px" Height="320px" 
           CssClass="modalPopupDialog">
    <div class="divCaption">Destinataires demande de modification :</div>
    <hr class="hrSimpleLine" />
    <div style="margin: 5px;">
        <table>
            <tr>
                <td>Utilisateurs :</td>
                <td>
                    <asp:DropDownList ID="ddlUserModif" runat="server" 
                        CssClass="ddlBlue" Style="width: 250px;">
                    </asp:DropDownList>
                </td>
                <td>
                    <div class="divBlueButton" onclick="requestAddMailModif();">Ajouter</div>
                </td>
            </tr>
            <tr>
                <td colspan="3">Liste des destinataires :</td>
            </tr>
            <tr>
                <td colspan="2" style="height: 170px; vertical-align: top; 
                    overflow-y: auto; border: 1px solid #b6b6b6;">
                    <asp:DataList ID="dlMailsModif" runat="server">
                        <ItemStyle CssClass="dlQualityUser" />
                        <ItemTemplate>
                            <div class="divUserItem" onclick="onMailModifClicked(this);">
                                <%# Eval("EMP_FIR_NAME") + " " + Eval("EMP_LAS_NAME") %>
                                <asp:HiddenField ID="hfMailCode" 
                                    Value='<%# Eval("EMP_EMAIL") %>' runat="server" />
                            </div>
                        </ItemTemplate>
                    </asp:DataList>
                </td>
                <td style="vertical-align: top;">
                    <div class="divBlueButton" onclick="requestDeleteMailModif();">Supprimer</div>
                </td>
            </tr>
        </table>
    </div>
    <hr class="hrSimpleLine" />
    <ul class="ulMenuHorizontalBlue">
        <li><a href="javascript:closeDialog('mainContent_mpeMailsModif');">Fermer</a></li>
    </ul>
</asp:Panel>

<%-- Extender pour les mails de modification --%>
<asp:HiddenField ID="hfMailsModif" runat="server" />
<ajaxToolkit:ModalPopupExtender ID="mpeMailsModif" runat="server"
    BackgroundCssClass="modalPopupBackground" 
    PopupControlID="panelMailsModif" 
    TargetControlID="hfMailsModif">
</ajaxToolkit:ModalPopupExtender>
