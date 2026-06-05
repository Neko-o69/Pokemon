<%@ Page Title="" Language="C#" MasterPageFile="~/Quality.master" AutoEventWireup="true" CodeFile="QualityQuery.aspx.cs" Inherits="QualityQuery" %>

<asp:Content ID="header" ContentPlaceHolderID="headContent" Runat="Server">

    <script src="Scripts/utility.js" ></script>
    <script src="Scripts/Quality.js" ></script>
    <script type="text/javascript">

        function pageLoad() {
            //var tbMasterSearch = document.getElementById('tbMasterSearch');
            //if (tbMasterSearch) {
            //    var fct = "onSearchKeyDown(event);";
            //    tbMasterSearch.onkeydown = new Function(fct);                
            //}

            var gvResult = document.getElementById('mainContent_gvResult');
            if (gvResult) gvResult.focus();
        }


        window.onscroll = function (event) {
            var docHeight = document.body.offsetHeight;
            docHeight = docHeight == undefined ? window.document.documentElement.scrollHeight : docHeight;

            var winheight = window.innerHeight;
            winheight = winheight == undefined ? document.documentElement.clientHeight : winheight;

            var scrollpoint = window.scrollY;
            scrollpoint = scrollpoint == undefined ? window.document.documentElement.scrollTop : scrollpoint;

            if ((scrollpoint + winheight) >= docHeight) {
                //alert("you're at the bottom");
                var hf = document.getElementById('<%=hfPageIndex.ClientID%>');
                var hfTotal = document.getElementById('<%=hfTotalPage.ClientID%>');
                var nPageIndex = parseInt(hf.value);
                var nTotalPage = parseInt(hfTotal.value);
                if (nPageIndex < nTotalPage) {
                    var nNewPageIndex = nPageIndex + 1;
                    hf.value = nNewPageIndex.toString();
                    requestData(nPageIndex);
                }
            }
        }

        function requestData(pageIndex) {
            // populate data from database
            $.ajax({
                url: "QualityQuery.aspx/PopulateDataByJava",
                data: "{pageNo: " + pageIndex + ", noOfRecord: 50}",
                type: "POST",
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                success: OnRequestDataSuccess,
                error: onError
            });
        }


        function OnRequestDataSuccess(data) {
            if (data.d == null) return;
            var gridView = document.getElementById('<%=gvResult.ClientID%>');
            if (gridView == null) return;
            var row = null;
            var d = data.d;
            for (var i = 0; i < d.length; i++) {
                //row = gridView.rows[2].cloneNode(true);
                row = gridView.insertRow(-1);
                var td = row.insertCell(0);
                var nType = parseInt(d[i].strType, 0);
                var div = createQueryDiv(d[i].strName, d[i].strDescription, nType, d[i].strFullPath);
                td.appendChild(div);
            }
        }


        function onItemClicked(nType, strFullPath) {
            if (nType <= 0) {
                window.location.href = "QualityFolder.aspx?folder=" + strFullPath + "";
            }
            else {
                        window.open("/Images/" + encodeURI(strFullPath), '_blank');
                }
        }
        

    </script>

</asp:Content>

<asp:Content ID="menu" ContentPlaceHolderID="menuContent" Runat="Server">
</asp:Content>

<asp:Content ID="main" ContentPlaceHolderID="mainContent" Runat="Server">
    <div class="divLine"></div>
    <h2>Résultat de la recherche :</h2>

    <asp:HiddenField runat="server" id="hfPageIndex"/>
    <asp:HiddenField runat="server" id="hfTotalPage"/>

    <asp:UpdatePanel ID="upResult" runat="server" UpdateMode="Conditional" ChildrenAsTriggers="true">
        <Triggers>
            <asp:AsyncPostBackTrigger ControlID="gvResult" EventName="" />
        </Triggers>
        <ContentTemplate>
            <asp:GridView ID="gvResult" runat="server" AutoGenerateColumns="false" GridLines="None" EnableViewState="true" OnRowDataBound="gvResult_RowDataBound">
                <Columns>
                    <asp:TemplateField>
                        <ItemStyle Width="980px" />
                        <ItemTemplate>
                            <div class="lbSearchQualityItem" onclick="onItemClicked(<%# Eval("BD_TYPE") %>, '<%# Eval("BD_FULL_PATH") %>')">
                                <table class="tableQualityItem">
                                    <tr>
                                        <td class="tdQualityImageItem">
                                            <asp:Image ID="imgFileType" runat="server" ImageUrl="~/Images/icon_folder_documents.png" Width="32px" Height="32px" />
                                            
                                        </td>
                                        <td class="tdQualityTextItem">
                                            Nom : <%# Eval("BD_NAME") %><br />
                                            Description : <%# Eval("BD_DESCRIPTION") %>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </ItemTemplate>
                    </asp:TemplateField>                    
                </Columns>
            </asp:GridView>
        </ContentTemplate>
    </asp:UpdatePanel>



    <script src="/Scripts/jquery-2.1.4.min.js"></script>

</asp:Content>

