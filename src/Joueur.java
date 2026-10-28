<%@ Page Title="" Language="C#" MasterPageFile="~/Quality.master" AutoEventWireup="true" CodeFile="QualityFolder.aspx.cs" Inherits="QualityFolder" %>


<asp:Content ID="header" ContentPlaceHolderID="headContent" runat="Server">
    
    <script src="/Scripts/jquery-2.1.4.min.js"></script>
    <script src="Scripts/utility.js"></script>
    <script src="Scripts/Quality.js"></script>
    <script src="Scripts/openseadragon.min.js"></script>
    <script type="text/javascript">

        var osdViewer = null;
        var isEDGE = false;
        var isIE = false;

        function pageLoad() {

            //showDragInfo();


            isIE = isIEBrowser();
            isEDGE = isEDGEBowser();

            //|------------------------------------------------------
            //|------------------------------------------------------
            populateAborescence();

            //|------------------------------------------------------
            // Modify l'item btnDelete
            //|------------------------------------------------------
            var btn = document.getElementById('btnFolder');
            if (btn) {
                var hfCurrentFolder = document.getElementById('<%= hfCurrentFolder.ClientID %>');
                if (hfCurrentFolder.value == 'Quality' || hfCurrentFolder.value == '') {
                    btn.text = 'Supprimer le dossier';
                    btn.style.display = 'none';




                }
                else {
                    btn.text = 'Supprimer le dossier : [' + hfCurrentFolder.value + ']';
                    btn.style.display = 'block';
                }


            }



            //|------------------------------------------------------
            // Init Image viewer
            //|------------------------------------------------------
            osdViewer = OpenSeadragon({
                id: "divImageViewer",
                //prefixUrl: "/images/",
                //tileSources: "/path/to/my/image.dzi"
                tileSources: {
                    type: 'image',
                    url: '/Images/no_big_image.png'
                }
            });

            //|------------------------------------------------------
            // Check if add new file
            //|------------------------------------------------------
            var hfAddNewFile = document.getElementById('mainContent_hfAddNewFile');
            if (hfAddNewFile && hfAddNewFile.value != "") {
                // Décomposer 
                var strName = "", strFullPath = "";
                var nType = 1;
                var nIndex = hfAddNewFile.value.indexOf('/');
                if (nIndex > 0) {
                    strName = hfAddNewFile.value.substring(0, nIndex);
                    var strType = hfAddNewFile.value.substring(nIndex + 1, nIndex + 2);
                    nType = parseInt(strType);
                    strFullPath = hfAddNewFile.value.substring(nIndex + 3);
                }

                var dataList = document.getElementById('mainContent_dlFolder');
                //var dataList = document.getElementById('mainContent_gvFolder');
                if (dataList == null) return;

                var row = dataList.insertRow(dataList.rows.length);
                var td = row.insertCell(0);

                var div = createDiv(strName, nType, strFullPath);
                td.appendChild(div);

                hfAddNewFile.value = "";
            }

            // Check user right
            requestUserRight();


            // select file ?
            // this vient généralement de la page QualityQuery
            var hfSelectedFile = document.getElementById('mainContent_hfSelectedFile');
            if (hfSelectedFile == null) return;
            if (hfSelectedFile.value.length <= 0) return;
            var hfSelectedType = document.getElementById('mainContent_hfSelectedType');
            if (hfSelectedType == null) return;
            if (hfSelectedType.value.length <= 0) return;

            hfSelectedRow = document.getElementById('mainContent_hfSelectedRow');
            if (hfSelectedRow == null) return;
            if (hfSelectedRow.value.length <= 0) return;

            var dataList = document.getElementById('mainContent_dlFolder');
            if (dataList == null) return;
            var nRow = parseInt(hfSelectedRow.value);
            if (nRow >= 0) {
                var div = dataList.rows[nRow].children[0].children[0];
                // Scroll first to d'abord
                dataList.scrollTop = div.clientHeight * nRow;
                var nType = parseInt(hfSelectedType.value);
                if (nType > 0) {
                    onItemClicked(div, nType, hfSelectedFile.value);
                }
            }


        }

        // end pageLoad

        //window.onbeforeunload = function () {
        //    // Destroy iFrame content
        //    var frame = $("#ifDoc");
        //    if (frame) frame.remove();
        //    if (isEDGE)
        //        return null;
        //}

        function newName(name) {
            // if (name == "ACH")
            //     return " Achat";
            // if (name == "MKT")
            //     return " Marketing";
            // if (name == "ADV")
            //     return " Administration Des Ventes";
            // if (name == "AUT")
            //     return " Autre";
            // if (name == "DAF")
            //     return "Finances";
            // if (name == "DRH")
            //     return "Ressources Humaines";
            // if (name == "GEN")
            //     return " Management";
            // if (name == "INF")
            //     return " Informatique";
            // if (name == "LOG")
            //     return " Logistique";
            // if (name == "TAB")
            //     return " Site de fabrication";
            // if (name == "TEC")
            //     return " Technique";
            // if (name == "VEN")
            //     return " Vente";
            // if (name == "DOC")
            //     return " Documents";
            // if (name == "FORM")
            //     return " Formulaires";
            // if (name == "MOP")
            //     return " Mode Opératoire";
            // if (name == "PROC")
            //     return " Procédure";
            // if (name == "REF")
            //     return " Référence";
            return name;
        }


        function populateAborescence() {

            var list = document.getElementById('ulAborescence');
            if (list == null) return;
            var hfFolder = document.getElementById('<%= hfFolderTree.ClientID %>');
            if (hfFolder.value == null ||
                hfFolder.value == "") return;

            var strFullPath = "";
            var init = true;
            var strFolder = hfFolder.value;
            var nIndex = strFolder.indexOf('/');
            while (nIndex >= 0) {
                var strItem = strFolder.substring(0, nIndex);
                var li = document.createElement("li");
                var a = document.createElement("a");
                a.href = "#"; // Default value
                if (init) {
                    strFullPath = strItem;
                    init = false;
                }
                else {
                    strFullPath = strFullPath + "/" + strItem;
                }
                if (strItem == 'Quality') {
                    a.text = '...';
                    a.href = "javascript:onItemClicked(null, 0, '')";
                }
                else {
                    a.text = newName(strItem);
                    a.href = "javascript:onItemClicked(null, 0, '" + strFullPath + "')";
                }

                li.appendChild(a);
                list.appendChild(li);
                strFolder = strFolder.substring(nIndex + 1);
                nIndex = strFolder.indexOf('/');

            }

            // Last item
            if (strFolder.length > 0) {
                li = document.createElement("li");
                var a = document.createElement("a");
                a.href = "#";
                if (init) {
                    strFullPath = strFolder;
                    init = false;
                }
                else {
                    strFullPath = strFullPath + "/" + strFolder;
                }
                if (strFolder == 'Quality') {
                    a.text = '...';
                    a.href = "javascript:onItemClicked(null, 0, '')";
                }
                else {
                    a.text = newName(strFolder);
                    a.href = "javascript:onItemClicked(null, 0, '" + strFullPath + "')";
                }
                a.value = "";
                li.appendChild(a);
                list.appendChild(li);
            }
        }

        var objLastSelected = null;
        function onItemClicked(obj, nType, strFullPath) {
            if (objLastSelected != null) {
                objLastSelected.className = "linkButtonQualityItem";

            }

            var hfSelected = document.getElementById('mainContent_hfSelectedFile');
            hfSelected.value = "";
            var hfSelectedType = document.getElementById('mainContent_hfSelectedType');
            hfSelectedType.value = nType;

            var iframe = document.getElementById('ifDoc');
            if (iframe == null) return;
            var divImgViewer = document.getElementById('divImageViewer');
            if (divImageViewer == null) return;

            //var strDisplay = divImageViewer.style.display;

            switch (nType) {
                //|------------------------------------------------------
                // Word ou Excel, ou PowerPoint
                //|------------------------------------------------------
                case 1: // doc
                case 2: // xls
                case 5: // ppt
                case 8: // xml

                    divImageViewer.style.display = 'none';
                    iframe.style.display = 'block';

                    showInfoDlg();
                    // Pour google
                    iframe.src = "https://docs.google.com/gview?url=http://nidek-w2k8-mob.nidek.fr:5004/Images/" + encodeURIComponent(strFullPath).replace(/%2F/g, "/") + "&embedded=true";
 
                    //iframe.src = "Images/" + encodeURI(strFullPath) ;

                    window.open("/Images/") + encodeURIComponent(strFullPath).replace(/%2F/g, "/"), '_blank);'



                    // Pour Microsoft                    
                    //iframe.src = "http://view.officeapps.live.com/op/view.aspx?src=" + window.location.host + "/Images/" + strFullPath;
                    //iframe.src = "http://view.officeapps.live.com/op/view.aspx?src=http://nidek-w2k8-mob.nidek.fr:5004/Images/" + encodeURI(strFullPath);


                    if (obj != null) {
                        objLastSelected = obj;
                        obj.className = "linkButtonQualityItemSelected";
                    }
                    hfSelected.value = strFullPath;
                    break;

                //|------------------------------------------------------
                // PDF
                //|------------------------------------------------------
                case 3:

                    divImageViewer.style.display = 'none';
                    iframe.style.display = 'block';

                    showInfoDlg();
                    // By self
                    iframe.src = "/Images/" + strFullPath;
                    // Par Google
                    //iframe.src = "http://docs.google.com/gview?url=" +
                    //    window.location.host + "/Images/" + strFullPath + "&embedded=true";
                    window.open("/Images/" + strFullPath, '_blank');
                    // Par Microsoft
                    //iframe.src = "http://view.officeapps.live.com/op/view.aspx?src=" +
                    //        window.location.host + "/Images/" + strFullPath;

                    if (obj != null) {
                        objLastSelected = obj;
                        obj.className = "linkButtonQualityItemSelected";
                    }
                    hfSelected.value = strFullPath;
                    break;

                //|------------------------------------------------------
                // Image
                //|------------------------------------------------------
                case 4:

                    divImageViewer.style.display = 'block';
                    iframe.style.display = 'none';

                    hideInfoDlg();

                    //|----------------------------------------------------
                    // Recharge l'image
                    //|----------------------------------------------------
                    if (osdViewer != null) {
                        //viewer.tileSources.url = "/images/" + strFullPath;
                        osdViewer.destroy();
                        osdViewer = OpenSeadragon({
                            id: "divImageViewer",
                            //prefixUrl: "/images/",
                            //tileSources: "/path/to/my/image.dzi"
                            tileSources: {
                                type: 'image',
                                url: "/images/" + strFullPath
                            }
                        });
                    }

                    if (obj != null) {
                        objLastSelected = obj;
                        obj.className = "linkButtonQualityItemSelected";
                    }
                    hfSelected.value = strFullPath;

                    break;

                //|------------------------------------------------------
                // Zip, Mpp
                //|------------------------------------------------------
                case 6:  //zip
                case 7: // mpp
                case 9://bin
                case 10://dwg
                    divImageViewer.style.display = 'none';
                    iframe.style.display = 'block';


                    // By self
                    iframe.style.display = 'none';
                    hideInfoDlg();
                    osdViewer.destroy();
                    //osdViewer.style.display = 'none';

                    if (obj != null) {
                        objLastSelected = obj;
                        obj.className = "linkButtonQualityItemSelected";
                    }
                    hfSelected.value = strFullPath;
                    break;

                default:    // C'est le folder
                    __doPostBack('gotoFolder', strFullPath);
                    hfSelected.value = "";
                    break;
            }
            //function doDrop(e, strFullPath, nType) {
            //    e.preventDefault();
            //    e.stopPropagation();
            //    var strSource = e.dataTransfer.getData("text");
            //    if (strSource == strFullPath) return false;
            //    if (nType != 0) return false;

            //    $.ajax({
            //        type: "POST",
            //        url: "QualityFolder.aspx/moveItem",
            //        data: "{'strSource':'" + strSource + "', 'strTarget':'" + strFullPath + "'}",
            //        contentType: "application/json; charset=utf-8",
            //        dataType: "json",
            //        success: function (response) {
            //            if (response.d == "true")
            //                location.reload();
            //            else
            //                showMsgBox(response.d, 'ATTENTION !', 'MsgBox');
            //        },
            //        error: onError
            //    });
            //    return false;
            //}
        }

    </script>

</asp:Content>
<asp:Content ID="menu" ContentPlaceHolderID="menuContent" runat="Server">
    <ul class="ulMenuVertical">
        <li id="menuCreateFolder" style="display: none;"><a href="javascript:showCreateFolderDlg();">Créer un sous-dossier</a></li>
        <li id="menuAddFile" style="display: none;"><a href="javascript:javascript:showUploadDlg();">Ajouter un fichier</a></li>
        <li id="menuDeleteFolder" style="display: none;"><a href="javascript:confirmDeleteFolder();">Supprimer</a></li>
        <li><a href="javascript:requestModif();">Demande de modification</a></li>
        <li><a href="javascript:downloadFile();">Télécharger</a></li>
        <%--<li><a href="javascript:showInfoDlg();">Test</a></li>--%>
    </ul>
</asp:Content>
<asp:Content ID="main" ContentPlaceHolderID="mainContent" runat="Server">


    <%--Pour debugage only--%>
    <div id="dragInfoDlg" class="divPopupMenuWhite" style="width: 100px; height: 30px;">
        <a id="dragInfo">????</a>
    </div>

    <asp:HiddenField ID="hfCurrentFolder" Value="" runat="server" />
    <asp:HiddenField ID="hfSelectedFile" Value="" runat="server" />
    <asp:HiddenField ID="hfSelectedType" Value="" runat="server" />
    <asp:HiddenField ID="hfFolderTree" Value="" runat="server" />
    <asp:HiddenField ID="hfAddNewFile" Value="" runat="server" />

    <%--Pour la provenance de QualityQuery.aspx--%>
    <asp:HiddenField ID="hfSelectedRow" Value="" runat="server" />


    <div class="divLine"></div>

    <%--Affichage l'aborescence --%>
    <table style="border-spacing: 0; border-collapse: collapse;">
        <tr>
            <td>
                <h2>Arborescence :</h2>
            </td>
            <td style="margin-left: 5px;">
                <ul id="ulAborescence" class="ulQuality">
                    <%--<li><a href="#">Dossier de référence</a></li>
                    <li><a href="#">Item 2</a></li>--%>
                </ul>
            </td>
        </tr>
    </table>

    
    
    <%-- Liste des folder & files --%>
    <table style="margin-top: 20px; border-spacing: 0; border-collapse: collapse;">
        <tr>
            <%--Colonne des éléments du repertoire--%>
            <td style="vertical-align: top; min-width: 255px; border-left: solid; border-width: 0.5px; color: #b6b6b6">
                <div id="dropZone" style="border:2px dashed grey; padding:20px; text-align:center; margin:10px 0; background:#f9f9f9;">
                        Glisser un fichier ici
                </div>
                <asp:UpdatePanel ID="upFiles" runat="server" UpdateMode="Conditional" ChildrenAsTriggers="true">

                    <ContentTemplate>
                        <asp:DataList ID="dlFolder" runat="server" OnItemDataBound="dlFolder_ItemDataBound">
                            <ItemStyle CssClass="dlQualityItem" />
                            <ItemTemplate>
                                <div class="linkButtonQualityItem"
                                    draggable="true"
                                    ondragstart="event.dataTransfer.setData('text', '<%# Eval("BD_FULL_PATH") %>')"
                                    ondragover="event.preventDefault(); event.stopPropagation(); return false;"
                                    ondrop="doDrop(event, '<%# Eval("BD_FULL_PATH") %>', <%# Eval("BD_TYPE") %>)"
                                    oncontextmenu="javascript:showPopupMenu(event, this, '<%# Eval("BD_FULL_PATH") %>'); return false;"
                                    onclick="onItemClicked(this, <%# Eval("BD_TYPE") %>, '<%# Eval("BD_FULL_PATH") %>')">
                                        <table class="tableQualityItem">
                                        <tr>
                                            <td class="tdQualityImageItem">
                                                <asp:Image ID="imgFileType" runat="server" ImageUrl="~/Images/icon_folder_documents.png" Width="32px" Height="32px" />
                                            </td>
                                            <td class="tdQualityTextItem">
                                                <%# Eval("BD_TYPE").ToString() == "0"
                                                    ? Eval("BD_NAME").ToString()
                                                        .Replace("INF","Informatique")
                                                        .Replace("DRH","Ressources Humaines")
                                                        .Replace("ACH","Achat")
                                                        .Replace("MKT","Marketing")
                                                        .Replace("AUT","Autre")
                                                        .Replace("ADV","Administration Des Ventes")
                                                        .Replace("VEN","Vente")
                                                        .Replace("TEC","Technique")
                                                        .Replace("LOG","Logistique")
                                                        .Replace("GEN","Management")
                                                        .Replace("DAF","Finances")
                                                        .Replace("TAB","Site de fabrication")
                                                        .Replace("DOC","Documents")
                                                        .Replace("FORM","Formulaires")
                                                        .Replace("MOP","Mode Opératoire")
                                                        .Replace("PROC","Procédure")
                                                        .Replace("REF","Document De Référence")
                                                    : Eval("BD_NAME")
                                                %>
                                            </td>
                                        </tr>
                                            
                                    </table>
                                    <asp:HiddenField ID="hfType" Value='<%# Eval("BD_TYPE") %>' runat="server" />
                                </div>
                            </ItemTemplate>
                        </asp:DataList>
                    </ContentTemplate>
                </asp:UpdatePanel>
            </td>

            <%--Colonne pour la vue du document--%>
            <td style="width: 1050px; height: 700px; vertical-align: top; border: 0.5px solid #b6b6b6;">
                

                <%--Pour affciher un PDF ou word, Excel...--%>
                <iframe id="ifDoc"
                    style="width: 1050px; height: 700px; display: none; position: fixed;"
                    onload="hideInfoDlg();"></iframe>
                <%--Pour affciher une image (jpeg, png, bmp, etc...--%>
                <div id="divImageViewer" style="width: 1050px; height: 700px; display: none;"></div>
            </td>
        </tr>
                        
        <%--<tr>
    <td class ="tdVersion" style=" text-align:end;">
        Version 10_2026
    </td>
</tr>--%>
    </table>


    <%----------------------------------------------------------------------------------%>
    <%--Panel pour le upload un fichier ou créer un dossier --%>
    <%----------------------------------------------------------------------------------%>
    <asp:Panel ID="panelUpload" runat="server" Width="450px" Height="260px" CssClass="modalPopupMsgBoxBorderBlue">
        <%--//|-- Le title--%>
        <div class="divDialogHeaderBlue">
            <asp:Label ID="lbTitleUpload" runat="server" CssClass="labelMsgBoxCaption" Text="Veuillez selectionner un fichier"></asp:Label>
        </div>

        <div style="width: 95%; height: 160px; margin-left: 5px; margin-top: 13px;">

            <asp:FileUpload ID="DocFileUpload" runat="server"  Style="display: none" onchange="onFileSelected();" />
            <table style="width: 100%;">
                <tr>
                    <td>
                        <asp:Label ID="lbFileName" runat="server" Text="Fichier :"></asp:Label></td>

                    <td style="width: 95%;">
                        <asp:TextBox ID="tbFileName" runat="server" Width="95%"></asp:TextBox></td>
                    <td>
                        <asp:LinkButton ID="lbOpen" runat="server"
                            CssClass="linkButtonMenu" Style="text-align: center; padding-top: 6px;" Width="110px" Height="20px">Parcourir...</asp:LinkButton>
                    </td>
                </tr>
                <tr>
                    <td>Affichage :</td>
                    <td colspan="2">
                        <asp:TextBox ID="tbDisplay" runat="server" Width="100%"></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td style="vertical-align: top;">Description :</td>
                    <td colspan="2">
                        <asp:TextBox ID="tbDescription" runat="server" Width="100%" Height="30px" TextMode="MultiLine"></asp:TextBox>
                    </td>
                </tr>
            </table>
            <asp:HiddenField ID="hfFile" runat="server" />

        </div>

        <div id="UploadFooter" style="margin-left: 10px">
            <ul class="ulMenuHorizontalBlue">
                <li><a href="#" onclick="onUploadOk();">Ok</a></li>
                <li><a href="#" onclick="closeUploadDlg();">Annuler</a></li>
            </ul>
        </div>

    </asp:Panel>

    <%----------------------------------------------------------------------------------%>
    <%-- Extender pour le popup  --%>
    <%----------------------------------------------------------------------------------%>
    <asp:HiddenField ID="hfUpload" runat="server" />
    <ajaxToolkit:ModalPopupExtender ID="mpeUpload" runat="server" BackgroundCssClass="modalPopupBackGround"
        PopupControlID="panelUpload" TargetControlID="hfUpload">
        <Animations>
            <OnShown>
                <ScriptAction Script="setFocusToTextBox('mainContent_tbFileName');" /> 
            </OnShown>
        </Animations>
    </ajaxToolkit:ModalPopupExtender>


    <%----------------------------------------------------------------------------------%>
    <%--Panel pour popup des message box--%>
    <%--NOTES : Panel est un DIV normalement invisible qui ne sera active que par Ajax--%>
    <%----------------------------------------------------------------------------------%>
    <asp:Panel ID="panelMsgBox" runat="server" Width="350px" Height="180px" CssClass="ModalPopupMsgBox">
        <asp:UpdatePanel ID="MsgBoxUpdatePanel" runat="server" UpdateMode="Conditional">
            <Triggers></Triggers>
            <ContentTemplate>
                <%--//|-- Le title--%>
                <div style="margin: 2px; background-color: #CC0000; color: #FFFFFF; font-family: 'Century Gothic'; font-size: small; font-weight: bold; text-align: center; height: 25px; padding-top: 4px;">
                    <asp:Label ID="lbMsgBoxTitle" runat="server" Text="ATTENTION"></asp:Label>
                </div>

                <div id="MsgBoxMessageDiv" style="width: 94%; height: 85px; margin-left: 10px; margin-top: 10px;">
                    <asp:Label ID="lbMsgBoxMessage" runat="server" ForeColor="Maroon"
                        Font-Bold="True" Text="Voulez vous vraiment supprimer ce devis ?"></asp:Label>
                    <asp:HiddenField ID="hfMsgBoxAction" Value="" runat="server" />
                    <br />
                </div>
            </ContentTemplate>
        </asp:UpdatePanel>

        <div id="MsgBoxFooterDiv" style="margin-left: 10px">
            <ul class="ulMenuHorizontalRed">
                <li><a href="javascript:onMsgBoxOk();">Ok</a></li>
                <li><a href="javascript:onMsgBoxCancel();">Annuler</a></li>
            </ul>
        </div>

    </asp:Panel>

    <%----------------------------------------------------------------------------------%>
    <%-- Extender pour le popup MsgBox --%>
    <%----------------------------------------------------------------------------------%>
    <asp:HiddenField ID="hfMsgBox" runat="server" />
    <ajaxToolkit:ModalPopupExtender ID="mpeMsgBox" runat="server"
        BackgroundCssClass="modalPopupBackGround" PopupControlID="panelMsgBox" TargetControlID="hfMsgBox">
    </ajaxToolkit:ModalPopupExtender>

    <%---------------------------------------------------------------------------------%>
    <%--Le menu contextuel pour chaque élément--%>
    <%---------------------------------------------------------------------------------%>
    <div id="linePopupMenu" class="divPopupMenu">
        <ul class="ulMenuVerticalDarkBlue">
            <li>
                <table style="border-spacing: 0; border-collapse: collapse;">
                    <tr>
                        <td>
                            <div class="divSmallCaption" style="width: 100px;">Menu</div>
                        </td>
                        <td>
                            <%--<img src="Images/icon_close.png" style="width:20px;height:20px;" onclick="closePopupMenu();" />--%>
                            <div class="divSmallCloseButton" onclick="closePopupMenu();"></div>
                        </td>
                    </tr>
                </table>
            </li>
            <li><a href="javascript:onMenuMoveUp();">Monter</a></li>
            <li><a href="javascript:onMenuMoveDown();">Descendre</a></li>
            <li><a href="javascript:onMenuDeleteItem();">Supprimer</a></li>
            <li>
                <hr class="hrSimpleLine" />
            </li>
            <li><a href="javascript:onMenuPropertie();">Propriétés</a></li>
            <li><a href="javascript:onMenuRight();">Droit d'accès</a></li>
        </ul>
    </div>

    <%----------------------------------------------------------------------------------%>
    <%-- Info bull pour information enregistrement --%>
    <%----------------------------------------------------------------------------------%>
    <div id="saveInfoDlg" class="divWaitDialog" onclick="hideInfoDlg();">
        <table style="margin: 10px; border-spacing: 0; border-collapse: collapse;">
            <tr>
                <td style="vertical-align: middle;">
                    <%--<img src="Images/loopingCircle.gif" style="width:64px; height:64px;"/>--%>
                    <div class="divLoader"></div>
                </td>
                <td style="vertical-align: middle;">
                    <asp:Label ID="saveInfoMessageLabel" runat="server" ForeColor="Maroon" Font-Size="120%"
                        Font-Bold="True" Text="Chargement en cours,<br/>Veuillez patientez..."></asp:Label>
                </td>
            </tr>
        </table>
    </div>
    <%--<asp:HiddenField ID="hfDisplayInfo" runat="server" Value="false" />--%>


    <%----------------------------------------------------------------------------------%>
    <%-- Formulaire demande de moficication --%>
    <%----------------------------------------------------------------------------------%>
    <asp:Panel ID="panelRequestModif" runat="server" Width="600px" Height="373px" CssClass="modalPopupDialog">
        <div class="divCaption">Demande de modification</div>
        <div style="margin: 5px;">
            <table>
                <tr>
                    <td style="width: 100px;">Destinataires :</td>
                    <td style="width: 550px;">
                        <asp:TextBox ID="tbTo" runat="server" Width="100%"></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td>Objet :</td>
                    <td>
                        <asp:TextBox ID="tbObject" runat="server" Width="100%"></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td>
                        <img src="Images/icon_attach.png" style="width: 16px; height: 16px;" />
                    </td>
                    <td>
                        <img src="Images/icon_pdf.png" style="width: 32px; height: 32px" id="imgAttach" />
                    </td>
                </tr>
                <tr>
                    <td style="vertical-align: top;">Description :</td>
                    <td>
                        <asp:TextBox ID="tbBody" runat="server" Width="100%" Height="150px" TextMode="MultiLine"></asp:TextBox>
                    </td>
                </tr>
            </table>
            <br />
            <ul class="ulMenuHorizontalBlue">
                <li><a href="javascript:doSendRequestModif();">Envoyer</a></li>
                <li><a href="javascript:closeDialog('mainContent_mpeRequestModif');">Annuler</a></li>
            </ul>
        </div>
    </asp:Panel>
    <%----------------------------------------------------------------------------------%>
    <%-- Extender pour le Request Modif --%>
    <%----------------------------------------------------------------------------------%>
    <asp:HiddenField ID="hfRequestModif" runat="server" />
    <ajaxToolkit:ModalPopupExtender ID="mpeRequestModif" runat="server"
        BackgroundCssClass="modalPopupBackGround" PopupControlID="panelRequestModif" TargetControlID="hfRequestModif">
        <Animations>
            <OnShown>
                <ScriptAction Script="setFocusToTextBox('mainContent_tbBody');" /> 
            </OnShown>
        </Animations>
    </ajaxToolkit:ModalPopupExtender>


    <%----------------------------------------------------------------------------------%>
    <%-- Feuille de propriétés --%>
    <%----------------------------------------------------------------------------------%>
    <asp:Panel ID="panelProperties" runat="server" Width="600px" Height="265px" CssClass="modalPopupDialog">
        <div class="divCaption">Propriétés</div>
        <div style="margin: 5px;">

            <table>
                <tr>
                    <td style="width: 100px;">Nom :</td>
                    <td style="width: 550px;">
                        <asp:TextBox ID="tbItemName" runat="server" Width="100%"></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td>EMail notif. :</td>
                    <td>
                        <asp:TextBox ID="tbItemEMail" runat="server" Width="100%"></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td style="vertical-align: top;">Description :</td>
                    <td>
                        <asp:TextBox ID="tbItemDescription" runat="server" Width="100%" Height="90px" TextMode="MultiLine"></asp:TextBox>
                    </td>
                </tr>
                <%--<tr>
                    <td>Ajouter:</td>
                    <td>
                        <asp:TextBox ID="tbEmpSearch" runat="server"></asp:TextBox>
                    </td>
                </tr>--%>
                <%--<tr style="height:150px;">
                    <td style="vertical-align:top;">Accès :</td>
                    <td style="vertical-align:top;">
                        <ul class="ulListBox" id="lstEmployes">
                            <li><a>Jean Makepevode</a></li>
                            <li><a>Jean Marc Maussion</a></li>
                        </ul>
                    </td>
                </tr>--%>
            </table>
            <hr class="hrSimpleLine" />
            <ul class="ulMenuHorizontalBlue">
                <li><a href="javascript:requestSaveProperties();">Enregistrer</a></li>
                <li><a href="javascript:closeDialog('mainContent_mpeProperties');">Annuler</a></li>
            </ul>
        </div>
    </asp:Panel>
    <%----------------------------------------------------------------------------------%>
    <%-- Extender pour le properties --%>
    <%----------------------------------------------------------------------------------%>
    <asp:HiddenField ID="hfProperties" runat="server" />
    <ajaxToolkit:ModalPopupExtender ID="mpeProperties" runat="server"
        BackgroundCssClass="modalPopupBackGround" PopupControlID="panelProperties" TargetControlID="hfProperties">
        <Animations>
            <OnShown>
                <ScriptAction Script="setFocusToTextBox('mainContent_tbItemName');" /> 
            </OnShown>
        </Animations>
    </ajaxToolkit:ModalPopupExtender>

    <%----------------------------------------------------------------------------------%>
    <%-- Droit d'accès --%>
    <%----------------------------------------------------------------------------------%>
    <asp:Panel ID="panelRight" runat="server" Width="450px" Height="370px" CssClass="modalPopupDialog">
        <div class="divCaption">Droit d'accès administration :</div>
        <hr class="hrSimpleLine" />
        <div style="margin: 5px;">
            <table>
                <tr>
                    <td style="width: 100px;">Dossier :</td>
                    <td colspan="2">
                        <asp:Label ID="lbFolder" runat="server" Text="Label" Font-Size="Medium" Font-Bold="true"></asp:Label>
                    </td>
                </tr>
                <tr>
                    <td>Utilisateurs :</td>
                    <td>
                        <asp:DropDownList ID="ddlUser" runat="server" CssClass="ddlBlue" Style="width: 250px;"></asp:DropDownList>
                    </td>
                    <td>
                        <div class="divBlueButton" onclick="requestAddUser();">Ajouter</div>
                    </td>
                </tr>
                <tr>
                    <td colspan="3">Liste des utilisateurs qui ont le droit d'accès :</td>
                </tr>
                <tr>
                    <td colspan="2" style="height: 170px; vertical-align: top; overflow-y: auto; border: 1px solid #b6b6b6;">
                        <%--<ul class="ulListBox" id="lstEmployes">
                            <li><a>Jean Makepevode</a></li>
                            <li><a>Jean Marc Maussion</a></li>
                        </ul>--%>
                        <asp:DataList ID="dlRight" runat="server">
                            <ItemStyle CssClass="dlQualityUser" />
                            <ItemTemplate>
                                <div class="divUserItem" onclick="onUserClicked(this);">
                                    <%# Eval("EMP_FIR_NAME") %> + " " <%# Eval("EMP_LAS_NAME") %>
                                    <asp:HiddenField ID="hfCode" Value='<%# Eval("BD_EMP_CODE") %>' runat="server" />
                                </div>
                            </ItemTemplate>
                        </asp:DataList>
                    </td>
                    <td style="vertical-align: top;">
                        <div class="divBlueButton" onclick="requestDeleteUser();">Supprimer</div>
                    </td>
                </tr>
            </table>
            <hr class="hrSimpleLine" />
            <ul class="ulMenuHorizontalBlue">
                <li><a href="javascript:closeDialog('mainContent_mpeRight');">Fermer</a></li>
            </ul>
        </div>
    </asp:Panel>
    <%----------------------------------------------------------------------------------%>
    <%-- Extender pour le droit d'accès --%>
    <%----------------------------------------------------------------------------------%>
    <asp:HiddenField ID="hfRight" runat="server" />
    <ajaxToolkit:ModalPopupExtender ID="mpeRight" runat="server"
        BackgroundCssClass="modalPopupBackGround" PopupControlID="panelRight" TargetControlID="hfRight">
    </ajaxToolkit:ModalPopupExtender>
 
    <script type="text/javascript">InitDragAndDrop();</script> 


                            <div class ="tdVersion" style=" text-align:end; color: black;">
    Version 10_2026
</div>

</asp:Content>

