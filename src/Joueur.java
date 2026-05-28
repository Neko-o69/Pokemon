<asp:DataList ID="dlFolder" runat="server" OnItemDataBound="dlFolder_ItemDataBound">
    <ItemStyle CssClass="dlQualityItem" />
    <ItemTemplate>
        <div class="linkButtonQualityItem"
            oncontextmenu="javascript:showPopupMenu(event, this, '<%# Eval("BD_FULL_PATH") %>'); return false;"
            onclick="onItemClicked(this, <%# Eval("BD_TYPE") %>, '<%# Eval("BD_FULL_PATH") %>')">
            <table class="tableQualityItem">
                <tr>
                    <td class="tdQualityImageItem">
                        <asp:Image ID="imgFileType" runat="server" ImageUrl="~/Images/icon_folder_documents.png" Width="32px" Height="32px" />
                    </td>
                    <td class="tdQualityTextItem">
                        <%# Eval("BD_NAME").ToString().Replace("INF","Informatique").Replace("DRH","Ressources Humaines").Replace("ACH","Achat").Replace("MKT","Marketing").Replace("AUT","Autre").Replace("ADV","Administration Des Ventes").Replace("VEN","Vente").Replace("TEC","Technique").Replace("LOG","Logistique").Replace("GEN","Management").Replace("DAF","Finances").Replace("TAB","Site de fabrication") %>                                                
                    </td>
                </tr>
            </table>
            <asp:HiddenField ID="hfType" Value='<%# Eval("BD_TYPE") %>' runat="server" />
        </div>
    </ItemTemplate>
</asp:DataList>
