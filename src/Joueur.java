var strFullPathForAdd = "";
var strFileNameForAdd = "";
var bMustRestorePDFViewer = false;

function getSelectedItemName() {
    if (objLastSelected == null) return "";
    var subTable = objLastSelected.getElementsByTagName('TABLE')[0];
    if (subTable == null) return "";
    return subTable.rows[0].children[1].innerHTML.trim();
}

function confirmDeleteFolder() {

    if (objLastSelected == null) return;
    //var strFolderName = objLastSelected.innerText.trim();
    var strFolderName = getSelectedItemName();
    showMsgBox('Vous allez supprimer le dossier [' + strFolderName + '] et tout son contenu !\r\nVoulez vous poursuivre ?', 'ATTENTION !', 'deleteFolder');
}

function confirmDeleteFile() {

    // Masque le pdf si exist
    var iFrame = document.getElementById('ifDoc');
    if (iFrame != null) {
        if (iFrame.style.display != 'none') {
            iFrame.style.display = 'none';
            bMustRestorePDFViewer = true;
        }
    }

    var hfSelected = document.getElementById('mainContent_hfSelectedFile');
    if (objLastSelected == null || hfSelected.value == '') {
        showMsgBox("Il n'y a rien à supprimer ?!?\r\nVeuillez selectionner un fichier.", "ERREUR !", "MsgBox");
        return;
    }

    var subTable = objLastSelected.getElementsByTagName('TABLE')[0];
    if (subTable == null) return;
    //var strFileName = subTable.rows[0].children[1].innerHTML;
    var strFileName = getSelectedItemName();
    showMsgBox("Voulez vous supprimer le fichier :\r\n" + strFileName.trim(), "ATTENTION !", "deleteFile");

}

function findItemIndex(dlFolder, item) {
    for (var i = 0; i < dlFolder.rows.length; i++) {
        var div = dlFolder.rows[i].children[0].children[0];
        if (div == item)
            return i;
    }
    return -1;
}

function createQueryDiv(strTitle, strDescription, nType, strFullPath) {
    var div = document.createElement('div');
    div.className = "lbSearchQualityItem";
    div.draggable = true;



    // Add new function onClick !
    var fct = "onItemClicked( " + nType + ", '" + strFullPath + "');";
    div.onclick = new Function(fct);

    var table = createQueryTable(strTitle, strDescription, nType);
    div.appendChild(table);

    return div;
}

function createQueryTable(strTitle, strDescription, nType) {
    var table = document.createElement('table');
    table.className = "tableQualityItem";
    var row = table.insertRow(0);

    var cell = row.insertCell(0);
    cell.className = "tdQualityImageItem";
    var img = createItemImage(nType); //|-- Type folder, word, excel, pdf, ....
    cell.appendChild(img);

    cell = row.insertCell(1);
    cell.className = "tdQualityTextItem";
    cell.innerHTML = 'Nom : ' + strTitle + '<br/>' + 'Description : ' + strDescription;
    return table;
}



function createDiv(strTitle, nType, strFullPath) {
    var div = document.createElement('div');
    div.className = "linkButtonQualityItem";
    div.draggable = true;

    // Add new function onClick !
    var fct = "onItemClicked( this, " + nType + ", '" + strFullPath + "');";
    div.onclick = new Function(fct);

    // oncontextmenu="javascript:showPopupMenu(event, this, '<%# Eval("BD_FULL_PATH") %>'); return false;"
    var ctxFct = "showPopupMenu(event, this, '" + strFullPath + "'); return false;";
    div.oncontextmenu = new Function(ctxFct);

    var table = createTable(strTitle, nType);
    div.appendChild(table);

    return div;
}

function createTable(strTitle, nType) {
    var table = document.createElement('table');
    table.className = "tableQualityItem";
    var row = table.insertRow(0);

    var cell = row.insertCell(0);
    cell.className = "tdQualityImageItem";
    var img = createItemImage(nType); //|-- Type folder, word, excel, pdf, ....
    cell.appendChild(img);

    cell = row.insertCell(1);
    cell.className = "tdQualityTextItem";
    cell.innerHTML = strTitle;
    return table;
}

function createItemImage(nType) {
    var img = document.createElement('img');
    img.width = 32;
    img.height = 32;
    switch (nType) {
        case 1:
            img.src = "Images/icon_word.png"
            break;
        case 2:
            img.src = "Images/icon_excel.png"
            break;
        case 3:
            img.src = "Images/icon_pdf.png"
            break;
        case 4:
            img.src = "Images/icon_image.png"
            break;
        case 5:
            img.src = "Images/icon_powerpoint.png"
            break;
        case 6:
            img.src = "Images/icon_winrar.png";
            break;
        case 7:
            img.src = "Images/icon_project.png";
            break;
        case 8:
            img.src = "Images/icon_xml.png"
            break;
        case 9:
            img.src = "Images/icon_bin.png"
            break;
        case 10:
            img.src = "Images/icon_dwg.png"
            break;
        default:
            img.src = "Images/icon_folder_documents.png"
            break;
    }
    return img;
}

function testAddRow() {
    var dataList = document.getElementById('mainContent_dlFolder');
    //var dataList = document.getElementById('mainContent_gvFolder');
    if (dataList == null) return;

    var row = dataList.insertRow(-1);
    var td = row.insertCell(0);
    var div = createDiv('New Folder for Test');
    td.appendChild(div);

    //|-----------------------------------------
    // Ok this marche
    // but not when datalist is empty !
    //|-----------------------------------------
    //var row = null;
    //row = dataList.rows[0].cloneNode(true);

    //var div = row.children[0].children[0];

    //// Add new function onClick !
    //var fct = "onItemClicked( this, 0, 'Quality/Qualité');";
    //div.onclick = new Function(fct);

    //var subTable = div.getElementsByTagName('TABLE')[0];
    //var img = div.getElementsByTagName('IMG')[0];
    //img.src = "Images/icon_folder_documents.png"

    //// (row 0, col 1) = nom du dossier
    //subTable.rows[0].children[1].innerText = "New Folder"; 

    //dataList.appendChild(row);

}

//|-------------------------------------------------------------
//| Les Call back
//| Pour gérer les return des ajax WebService
//|-------------------------------------------------------------

function onError(error) {
    alert('SYSTEM ERROR : ' + error);
}

function OnAddFolderSuccessful(response) {
    if (response.d == "") return;
    if (response.d != "true") {
        showMsgBox(response.d, 'ATTENTION !', 'MsgBox');
        return;
    }

    //|-- Add successfull, load this folder into list
    var dataList = document.getElementById('mainContent_dlFolder');
    //var dataList = document.getElementById('mainContent_gvFolder');
    if (dataList == null) return;

    var row = dataList.insertRow(-1);
    var td = row.insertCell(0);
    var div = createDiv(strFileNameForAdd, 0, strFullPathForAdd);
    td.appendChild(div);

}

function OnAddFileSuccessful(resoponse) {
    if (response.d == "") return;
    if (response.d != "true") {
        showMsgBox(response.d, 'ATTENTION !', 'MsgBox');
        return;
    }

    showMsgBox('Add new file Ok !', 'SUCCESS!', 'MsgBox');
}

function OnDeleteFolderSuccessful(response) {
    if (response.d == "") return;
    if (response.d != "true") {
        showMsgBox(response.d, 'ATTENTION !', 'MsgBox');
        return;
    }

    //|-- Delete successful, goto parent folder
    var hf = document.getElementById('mainContent_hfFolderTree');
    var strParentFolder = hf.value;
    var nIndex = strParentFolder.lastIndexOf('/');
    if (nIndex >= 0)
        strParentFolder = strParentFolder.substring(0, nIndex);
    __doPostBack('gotoFolder', strParentFolder);

}

function onDeleteFileSuccessful(response) {
    if (response.d == "") return;
    if (response.d != "true") {
        showMsgBox(response.d, 'ATTENTION !', 'MsgBox');
        return;
    }


    // Ok ça marche mais one shoot
    //// Remove from datalist
    //var hfRowIndex = objLastSelected.getElementsByTagName('INPUT')[0];
    //if (hfRowIndex == null) return;
    //var nRowIndex = hfRowIndex.value;

    var dataList = document.getElementById('mainContent_dlFolder');
    //var dataList = document.getElementById('mainContent_gvFolder');
    if (dataList == null) return;

    var nRowIndex = findItemIndex(dataList, objLastSelected);
    if (nRowIndex > 0)
        dataList.deleteRow(nRowIndex);
}



//*--------------------------------------------------------------------
// Pour la boite Dialog Upload
//*--------------------------------------------------------------------
function showCreateFolderDlg() {
    var dlg = $find('mainContent_mpeUpload');
    if (dlg == null) return;
    var lb = document.getElementById('mainContent_lbTitleUpload');
    if (lb) lb.innerText = "Veuillez spécifier un nom du sous dossier à créer";

    lb = document.getElementById('mainContent_lbFileName');
    if (lb) lb.innerText = 'Dossier :';

    lb = document.getElementById('mainContent_lbOpen');
    if (lb) lb.style.display = 'none';

    var tb = document.getElementById('mainContent_tbFileName');
    if (tb) tb.value = '';
    tb = document.getElementById('mainContent_tbDisplay');
    if (tb) tb.value = '';
    tb = document.getElementById('mainContent_tbDescription');
    if (tb) tb.value = '';

    //|----------------------------------------------------------
    // Flag pour savoir quelle action pour onUploadOk
    //|----------------------------------------------------------
    var hf = document.getElementById('mainContent_hfFile');
    if (hf) hf.value = 'folder';


    dlg.show();
}

function onMoveUpSuccessful(response) {

    if (response.d == "") return; // nothing to do
    if (response.d != "true") {
        showMsgBox(response.d, 'ATTENTION !', 'MsgBox');
        return;
    }

    // Swap
    if (m_nCurrentLineIndex < 0) return;
    var dataList = document.getElementById('mainContent_dlFolder');
    if (dataList == null) return;
    //dataList.moveRow(m_nCurrentLineIndex, m_nCurrentLineIndex - 1);
    // On swap les rows
    var divOriginToRemove = dataList.rows[m_nCurrentLineIndex].children[0].children[0];
    var divOrigin = divOriginToRemove.cloneNode(true);
    var divTargetToRemove = dataList.rows[m_nCurrentLineIndex - 1].children[0].children[0];
    var divTarget = divTargetToRemove.cloneNode(true);

    dataList.rows[m_nCurrentLineIndex].children[0].removeChild(divOriginToRemove);
    dataList.rows[m_nCurrentLineIndex].children[0].appendChild(divTarget);

    dataList.rows[m_nCurrentLineIndex - 1].children[0].removeChild(divTargetToRemove);
    dataList.rows[m_nCurrentLineIndex - 1].children[0].appendChild(divOrigin);

    // Important !
    objLastSelected = dataList.rows[m_nCurrentLineIndex - 1].children[0].children[0];

}

function onMoveDownSuccessful(response) {

    if (response.d == "") return; // nothing to do
    if (response.d != "true") {
        showMsgBox(response.d, 'ATTENTION !', 'MsgBox');
        return;
    }

    // Swap
    if (m_nCurrentLineIndex < 0) return;
    var dataList = document.getElementById('mainContent_dlFolder');
    if (dataList == null) return;
    //dataList.moveRow(m_nCurrentLineIndex, m_nCurrentLineIndex + 1);

    // On swap les rows
    var divOriginToRemove = dataList.rows[m_nCurrentLineIndex].children[0].children[0];
    var divOrigin = divOriginToRemove.cloneNode(true);
    var divTargetToRemove = dataList.rows[m_nCurrentLineIndex + 1].children[0].children[0];
    var divTarget = divTargetToRemove.cloneNode(true);

    dataList.rows[m_nCurrentLineIndex].children[0].removeChild(divOriginToRemove);
    dataList.rows[m_nCurrentLineIndex].children[0].appendChild(divTarget);

    dataList.rows[m_nCurrentLineIndex + 1].children[0].removeChild(divTargetToRemove);
    dataList.rows[m_nCurrentLineIndex + 1].children[0].appendChild(divOrigin);

    // Important !
    objLastSelected = dataList.rows[m_nCurrentLineIndex + 1].children[0].children[0];

}

function showUploadDlg() {
    var dlg = $find('mainContent_mpeUpload');
    if (dlg == null) return;

    var lb = document.getElementById('mainContent_lbTitleUpload');
    if (lb) lb.innerText = "Veuillez selectionner un fichier";

    lb = document.getElementById('mainContent_lbFileName');
    if (lb) lb.innerText = 'Fichier :';

    lb = document.getElementById('mainContent_lbOpen');
    if (lb) lb.style.display = 'inline-block';

    var tb = document.getElementById('mainContent_tbFileName');
    if (tb) tb.value = '';
    tb = document.getElementById('mainContent_tbDisplay');
    if (tb) tb.value = '';
    tb = document.getElementById('mainContent_tbDescription');
    if (tb) tb.value = '';

    //|----------------------------------------------------------
    // Flag pour savoir quelle action pour onUploadOk
    //|----------------------------------------------------------
    var hf = document.getElementById('mainContent_hfFile');
    if (hf) hf.value = 'file';

    dlg.show();
}

function closeUploadDlg() {
    var dlg = $find('mainContent_mpeUpload');
    if (dlg == null) return;
    dlg.hide();
}

function callMenu(btnName) {
    __doPostBack('executeMenu', btnName);
}

function downloadFile() {

    if (objLastSelected == null) return;

    var hfSelectedType = document.getElementById('mainContent_hfSelectedType');
    if (hfSelectedType == null) return;
    if (hfSelectedType.value == "-1" || hfSelectedType.value == "0")
        return;

    var hfSelected = document.getElementById('mainContent_hfSelectedFile');
    if (hfSelected.value == '') {
        showMsgBox("Il n'y a rien à télécharger ?!?\r\nVeuillez selectionner un fichier.", "ERREUR !", "MsgBox");
        return;
    }

    __doPostBack('downloadFile', hfSelected.value);

}

function checkAndUploadFile() {
    var tbFolderName = document.getElementById('mainContent_tbFileName');
    if (tbFolderName == null) return;
    if (tbFolderName.value == '') {
        showMsgBox('Le nom du du fichier est vide !\r\nVous devez spécifier un nom de fichier.', 'ERREUR !', 'MsgBox');
        return;
    }

    // Ok upload file now...
    var strFileName = tbFolderName.value;
    var nIndex = strFileName.lastIndexOf('.');
    if (nIndex < 0) {
        showMsgBox('Le type de fichier inconnu !\r\nLes fichiers valables sont Word, Pdf, Excel, Images.', 'ERREUR !', 'MsgBox');
        return;
    }

    var strExt = strFileName.substring(nIndex + 1).toUpperCase();
    switch (strExt) {
        case 'DOC':
        case 'DOCX':
            break;

        case 'XLS':
        case 'XLSX':
            break;

        case 'PPT':
        case 'PPTX':
            break;

        case 'PDF':
            break;

        case 'PNG':
        case 'JPG':
        case 'JPEG':
        case 'GIF':
        case 'BMP':
            break;

        case 'XML':
            break;
        case 'BIN':
            break;
        case 'DWG':
            break;

        default:
            showMsgBox('Le type de fichier [' + strExt + '] n\'est pas connu !\r\nLes fichiers valables sont Word, Pdf, Excel, Images.', 'ERREUR !', 'MsgBox');
            return;
    }

    __doPostBack('uploadFile', '');

}

function checkAndCreateFolder() {
    var tbFolderName = document.getElementById('mainContent_tbFileName');
    if (tbFolderName == null) return;
    if (tbFolderName.value == '') {
        showMsgBox('Le nom du dossier est vide !\r\nVous devez spécifier un nom du sous dossier.', 'ERREUR !', 'MsgBox');
        return;
    }

    // Ok create folder now...
    var strDisplay = "", strDescription = "";
    var tb = document.getElementById('mainContent_tbDisplay');
    if (tb) strDisplay = tb.value;
    if (strDisplay == "") strDisplay = tbFolderName.value;

    tb = document.getElementById('mainContent_tbDescription');
    if (tb) strDescription = tb.value;
    if (strDescription == "") strDescription = tbFolderName.value;

    var hf = document.getElementById('mainContent_hfFolderTree');
    var strParentFolder = hf.value;
    if (strParentFolder == '' || strParentFolder == '...')
        strParentFolder = 'Quality';

    strFullPathForAdd = strParentFolder + '/' + tbFolderName.value;
    strFileNameForAdd = tbFolderName.value;
    tbFolderName.value = tbFolderName.value.replace(/'/g, "\u2019").replace(/'/g, "\u0025");
    strDisplay = strDisplay.replace(/'/g, "\u2019").replace(/'/g, "\u0025");
    strDescription = strDescription.replace(/'/g, "\u2019").replace(/'/g, "\u0025");



    // Post bast to web service pour créer un sous dossier
    $.ajax({
        url: "QualityFolder.aspx/addNewFolder",
        data: "{'strParentFolder':'" + strParentFolder + "', 'strFolder':'" + tbFolderName.value + "', 'strDisplay': '" + strDisplay + "', 'strDescription':'" + strDescription + "'}",
        type: "POST",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: OnAddFolderSuccessful,
        error: onError
    });

}

function onUploadOk() {

    closeUploadDlg()
    //callMenu('btnUploadOk');
    // Check ici si toutyes les données sont nécessaies pour création fichier ou dossier
    var hf = document.getElementById('mainContent_hfFile');
    if (hf.value == 'file') {
        checkAndUploadFile();
    }
    else {
        checkAndCreateFolder();
    }

}
function onFileSelected() {
    document.getElementById('mainContent_tbFileName').value = document.getElementById('mainContent_DocFileUpload').value;
    console.log('test');
}

//*--------------------------------------------------------------------
// Pour la suppression des dossier
//*--------------------------------------------------------------------
function doDeleteFolder() {
    var hf = document.getElementById('mainContent_hfSelectedFile');
    if (hf == null) return;

    // Post back to web service pour créer un sous dossier
    $.ajax({
        url: "QualityFolder.aspx/deleteFolder",
        data: "{'strFullPath':'" + hf.value + "'}",
        type: "POST",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: OnDeleteFolderSuccessful,
        error: onError
    });

}

//*--------------------------------------------------------------------
// Pour la suppression un fichier
//*--------------------------------------------------------------------
function doDeleteFile() {

    var hfSelected = document.getElementById('mainContent_hfSelectedFile');
    if (hfSelected.value == '') return;
    var strFullPath = hfSelected.value;
    __doPostBack('deleteFile', strFullPath);

    //|---------------------------------------------------------
    // This run only one shoot, cause index not re sort !
    // Abandon, mais keep this in mind, maybe userful later
    //|---------------------------------------------------------
    $.ajax({
        url: "QualityFolder.aspx/deleteFile",
        data: "{'strFullPath':'" + hfSelected.value + "'}",
        type: "POST",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: onDeleteFileSuccessful,
        error: onError
    });

}


//*--------------------------------------------------------------------
// Pour gérer le MessageBox en Java & C#
//*--------------------------------------------------------------------
var bCanPostback = true; // Flag pour gestion en Java ou C#
var strMsgBoxReturnAction = 'MsgBox'; // Quelle est action pour le return du MsgBox ?
function showMsgBox(strMessage, strTitle, strAction) {

    bCanPostback = false;

    var lb = document.getElementById('mainContent_lbMsgBoxMessage');
    if (lb) lb.innerText = strMessage;

    lb = document.getElementById('mainContent_lbMsgBoxTitle');
    if (lb) lb.innerText = strTitle;

    strMsgBoxReturnAction = strAction;

    showDialog('mainContent_mpeMsgBox');
}

function restorePDFViewer() {
    bMustRestorePDFViewer = false;
    var iFrame = document.getElementById('ifDoc');
    if (iFrame != null)
        iFrame.style.display = 'block';
}

function onMsgBoxOk() {
    if (bCanPostback == true) {
        callMenu('btnMsgBoxOk');
        return;
    }

    closeDialog('mainContent_mpeMsgBox');
    if (bMustRestorePDFViewer == true) restorePDFViewer();

    switch (strMsgBoxReturnAction) {
        case 'deleteFolder':
            doDeleteFolder();
            break;
        case 'deleteFile':
            doDeleteFile();
            break;
        default:
            break;
    }
}
function onMsgBoxCancel() {
    if (bCanPostback == true) {
        callMenu('btnMsgBoxCancel');
        return;
    }

    if (bMustRestorePDFViewer == true) restorePDFViewer();
    closeDialog('mainContent_mpeMsgBox');
}


//|-------------------------------------------------
//|-- Gestion menu contextuel de chaque item
//|-------------------------------------------------
var isIE = isIEBrowser(); //document.all ? true : false;
var m_nCurrentLineIndex = -1;
var m_currentX, m_currentY;
function showPopupMenu(e, obj, strFullPath) {

    if (bUserRight == false) return; // No right

    if (objLastSelected != null) {
        objLastSelected.className = "linkButtonQualityItem";
    }
    objLastSelected = obj;
    obj.className = "linkButtonQualityItemSelected";

    // Save pour plutard
    var hfSelected = document.getElementById('mainContent_hfSelectedFile');
    hfSelected.value = strFullPath;
    var hfSelectedType = document.getElementById('mainContent_hfSelectedType');
    if (hfSelectedType == null) return;
    var hfType = objLastSelected.getElementsByTagName('INPUT')[0];
    if (hfType == null) return;
    hfSelectedType.value = hfType.value;



    var el, x, y;

    //m_strLineInfo = lineInfo;
    el = document.getElementById('linePopupMenu');

    if (!isIE) {
        x = e.pageX;
        y = e.pageY;
    }
    if (isIE) {
        x = event.clientX + document.body.scrollLeft;
        y = event.clientY + document.body.scrollTop;
    }

    m_currentX = x;
    m_currentY = y;

    el.style.left = x + "px";
    el.style.top = y + "px";
    el.style.display = "block";

}

function closePopupMenu() {
    var el = document.getElementById('linePopupMenu');
    el.style.display = 'none';
}

function doClosePopupMenu() {
    var el = document.getElementById('linePopupMenu');
    el.style.display = 'none';
    return false;
}

function onMenuDeleteItem() {
    closePopupMenu();
    if (objLastSelected == null) return;
    var hf = objLastSelected.getElementsByTagName('INPUT')[0];
    if (hf != null) {
        //if(hf.value == '0')
        //    confirmDeleteFolder();
        //else
        //    confirmDeleteFile();
        switch (hf.value) {
            case '-1':
                showMsgBox('Vous ne pouvez pas supprimer un dossier système !', 'ATTENTION !', 'MsgBox');
                break;
            case '0':
                confirmDeleteFolder();
                break;
            default:
                confirmDeleteFile();
                break;
        }
    }
}

function onMenuMoveUp() {
    closePopupMenu();
    var dataList = document.getElementById('mainContent_dlFolder');
    if (dataList == null) return;
    var nRowIndex = findItemIndex(dataList, objLastSelected);
    m_nCurrentLineIndex = nRowIndex; // Save pour onMoveSuccessfull
    $.ajax({
        type: "POST",
        url: "QualityFolder.aspx/moveLineUp",
        data: "{'strLineNumber': '" + nRowIndex + "'}",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: onMoveUpSuccessful,
        failure: onError
    });
}

function onMenuMoveDown(strFullPath) {
    closePopupMenu();
    var dataList = document.getElementById('mainContent_dlFolder');
    if (dataList == null) return;
    var nRowIndex = findItemIndex(dataList, objLastSelected);
    m_nCurrentLineIndex = nRowIndex; // Save pour onMoveSuccessfull
    $.ajax({
        type: "POST",
        url: "QualityFolder.aspx/moveLineDown",
        data: "{'strLineNumber': '" + nRowIndex + "'}",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: onMoveDownSuccessful,
        failure: onError
    });
}


//|----------------------------------------------
//| Gestion de la boite de dialog d'information
//|----------------------------------------------
function showInfoDlg() {
    // show l'info ou pas !
    var infoDlg = document.getElementById('saveInfoDlg');
    if (infoDlg) {

        infoDlg.style.left = window.innerWidth / 2 - 150 + "px";
        infoDlg.style.top = window.innerHeight / 2 - 50 + "px";
        infoDlg.style.display = 'block';
        window.location.hash = 'saveInfoDlg';
    }
}

function hideInfoDlg() {
    var infoDlg = document.getElementById('saveInfoDlg');
    if (infoDlg) {
        infoDlg.style.display = 'none';
    }
}

//|----------------------------------------------
//| Demande de modification d'un document
//|----------------------------------------------
function requestModif() {
    if (objLastSelected == null) return;

    var hfSelectedFile = document.getElementById('mainContent_hfSelectedFile');
    if (hfSelectedFile == null) return;
    var hfSelectedType = document.getElementById('mainContent_hfSelectedType');
    if (hfSelectedType == null) return;
    if (hfSelectedType.value == "-1" || hfSelectedType.value == "0")
        return;

    $.ajax({
        type: "POST",
        url: "QualityFolder.aspx/requestFolderInformation",
        data: "{'strFullPath': '" + hfSelectedFile.value + "'}",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: onRequestFolderSuccessful,
        failure: onError
    });
}

function onRequestFolderSuccessful(response) {
    if (response.d == "") return; // nothing to do

    // Masque le iFrame
    var iframe = document.getElementById('ifDoc');
    if (iframe != null) iframe.style.display = 'none';

    // En mode dev, l'event onload de iFrame ne se déclencha pas !
    // donc il faut appeller ici
    hideInfoDlg();

    var strRetCode = response.d.substring(0, 5);
    if (strRetCode == 'ERROR') {
        showMsgBox(response.d, 'ATTENTION !', 'MsgBox');
        return;
    }

    var nIndex = response.d.indexOf('/');
    if (nIndex < 0) {
        showMsgBox('No information for this folder !', 'ERROR !', 'MsgBox');
        return;
    }

    // Ok extrait le destinataire & emmeteur
    var strDestiny = response.d.substring(0, nIndex);
    var strFrom = response.d.substring(nIndex + 1);

    //showMsgBox('Destinataire : ' + strDestiny + ', Emmeteur : ' + strFrom, 'ATTENTION !', 'MsgBox');
    var dlg = document.getElementById('mainContent_panelRequestModif');
    if (dlg == null) return;

    if (objLastSelected == null) return;
    var strFileName = objLastSelected.innerText.trim();


    var tb = document.getElementById('mainContent_tbFrom');
    if (tb) tb.value = strFrom;
    tb = document.getElementById('mainContent_tbTo');
    if (tb) tb.value = strDestiny;
    tb = document.getElementById('mainContent_tbObject');
    if (tb) tb.value = 'Demande de modification du fichier : ' + strFileName;
    tb = document.getElementById('mainContent_tbBody');
    if (tb) tb.value = '';

    // Image du fichier d'attach
    var img = document.getElementById('imgAttach');
    if (img) {
        var hfSelectedType = document.getElementById('mainContent_hfSelectedType');
        if (hfSelectedType == null) return;
        switch (hfSelectedType.value) {
            case '1':
                img.src = "Images/icon_word.png"
                break;
            case '2':
                img.src = "Images/icon_excel.png"
                break;
            case '3':
                img.src = "Images/icon_pdf.png"
                break;
            case '4':
                img.src = "Images/icon_image.png"
                break;
            case '5':
                img.src = "Images/icon_powerpoint.png"
                break;
            case '8':
                img.src = "Images/icon_xml.png"
                break;
            //default:
            //    // Not permis here
            //    return;
        }
    }

    showDialog('mainContent_mpeRequestModif');
}

function closeRequestDialog() {
    closeDialog('mainContent_mpeRequestModif');
}

function doSendRequestModif() {

    var hfSelectedFile = document.getElementById('mainContent_hfSelectedFile');
    if (hfSelectedFile == null) return;
    var tbTo = document.getElementById('mainContent_tbTo');
    if (tbTo == null) return;
    var tbObject = document.getElementById('mainContent_tbObject');
    if (tbObject == null) return;
    var tbBody = document.getElementById('mainContent_tbBody');
    if (tbBody == null) return;
    var strBody = tbBody.value.split('\'').join('|');
    closeDialog("mainContent_mpeRequestModif");

    $.ajax({
        type: "POST",
        url: "QualityFolder.aspx/sendRequestModification",
        data: "{'strTo':'" + tbTo.value + "', 'strObject':'" + tbObject.value + "', 'strBody':'" + strBody + "', 'strAttach':'" + hfSelectedFile.value + "'}",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: onSendRequestSuccessful,
        failure: onError
    });

}

function onSendRequestSuccessful(response) {

    if (response.d == "") return; // nothing to do
    if (response.d != "true") {
        showMsgBox(response.d, 'ATTENTION !', 'MsgBox');
        return;
    }

    showMsgBox('Demande envoyé !', 'Ok !', 'MsgBox');
}


// Pour le login
function checkUser() {
    var tbUser = document.getElementById('mainContent_tbUser');
    if (tbUser == null) return;
    var tbPW = document.getElementById('mainContent_tbPW');
    if (tbPW == null) return;
 
    $.ajax({
        type: "POST",
        url: "QualityLogin.aspx/login",
        //data: "{'strUser':'" + tbUser.value + "', 'strPassword':'" + tbPW.value + "'}",
        data: JSON.stringify({ strUser: tbUser.value, strPassword: tbPW.value }),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: onLoginSuccessful,
        failure: onError
    });
}

//pour changer mot de passe
function checkPW() {
    var tbOldPW = document.getElementById('mainContent_tbOldPW');
    if (tbOldPW == null) return;
    var tbNewPW = document.getElementById('mainContent_tbNewPW');
    if (tbNewPW == null) return;
    var tbConfirmPW = document.getElementById('mainContent_tbConfirmPW');
    if (tbConfirmPW == null) return;

    $.ajax({
        type: "POST",
        url: "QualityPassword.aspx/changePassword",
        data: "{'strOldPW':'" + tbOldPW.value + "', 'strNewPW':'" + tbNewPW.value + "', 'strConfirmPW':'" + tbConfirmPW.value + "'}",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: onPWSuccessful,
        failure: onErrorPW
    });
}
function onErrorPW(response) {
    if (response.d != "true") {
        showMsgBox(response.d, "ATTENTION !", "MsgBox");
        return;
    }
}
function onPWSuccessful(response) {
    console.log(response.d);
    if (response.d != "true") {
        showMsgBox(response.d, "ATTENTION !", "MsgBox");
        return;
    }
    else {
        showMsgBox("Le mot de passe à été modifié", "ATTENTION !", "MsgBox");
        //return;
        window.location.href = 'QualityHome.aspx';
    }

}

function onLoginSuccessful(response) {
    console.log(response.d);
    if (response.d != "true") {
        showMsgBox("Le nom d'utilisateur ou le mot de pass erroné", "ATTENTION !", "MsgBox");
        return;
    }

    window.location.href = 'QualityHome.aspx';
}

//|---------------------------------------
// Pour le droit
//|---------------------------------------
var bUserRight = false; // Droit d'accès simplement au dossier spécifié
var bUserAdmin = false; // Pour l'attribution de droit d'accès et email de modification (accès total)
function onRequestUserRightOk(response) {

    if (response.d != "true" && response.d != "admin") {
        // Ok no right, nothin to do...
        return;
    }

    bUserRight = true;
    if (response.d == "admin")
        bUserAdmin = true; // Pour l'attribution de droit d'accès et email de modification

    // Maintenant activer les menu pour admin
    var li = document.getElementById('menuCreateFolder');
    if (li) li.style.display = 'inline';
    li = document.getElementById('menuAddFile');
    if (li) li.style.display = 'inline';
    li = document.getElementById('menuDeleteFile');
    if (li) li.style.display = 'inline';
}

function requestUserRight() {

    var hfFolder = document.getElementById('mainContent_hfFolderTree');
    if (hfFolder == null) return; // Ok no right

    $.ajax({
        type: "POST",
        url: "QualityFolder.aspx/loadUserRight",
        data: "{'strFullPath':'" + hfFolder.value + "'}",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: onRequestUserRightOk,
        failure: onError
    });

}


function onSearchKeyDown(event) {
    if (event.keyCode == 13) {
        var tbSearch = document.getElementById('tbMasterSearch');
        if (tbSearch) {
            if (tbSearch.value.length <= 0) return true;

            var strCurrentPage = "";
            var nIndex = window.location.href.lastIndexOf('/');
            if (nIndex > 0) {
                strCurrentPage = window.location.href.substring(nIndex + 1);
                nIndex = strCurrentPage.indexOf('?');
                if (nIndex > 0) {
                    strCurrentPage = strCurrentPage.substring(0, nIndex);
                }
            }

            if (strCurrentPage == 'QualityQuery.aspx')
                __doPostBack('requery', tbSearch.value);
            else
                window.location.href = "QualityQuery.aspx?query=" + tbSearch.value + "";
            return false;
        }
    }
}


//|---------------------------------------
// Boite de dialog pour propriétés
//|---------------------------------------
var objPropertiesItem = null;
function onMenuPropertie() {
    closePopupMenu();

    // Masque le pdf si exist
    var iFrame = document.getElementById('ifDoc');
    if (iFrame != null) {
        if (iFrame.style.display != 'none') {
            iFrame.style.display = 'none';
            bMustRestorePDFViewer = true;
        }
    }

    // Save pour plutard
    var hfSelected = document.getElementById('mainContent_hfSelectedFile');
    if (hfSelected == null) return;

    objPropertiesItem = null;

    $.ajax({
        type: "POST",
        url: "QualityFolder.aspx/requestProperties",
        data: "{'strFullPath':'" + hfSelected.value + "'}",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: onRequestPropertiesOk,
        failure: onError
    });

}

function onRequestPropertiesOk(reponse) {
    if (reponse.d == null) return;
    var data = reponse.d;
    if (data.length <= 0) return;

    // Save for later pour le save
    objPropertiesItem = data;

    // Ok remplir le formulaire
    var tb = document.getElementById('mainContent_tbItemName');
    if (tb) tb.value = data[0].strName;

    tb = document.getElementById('mainContent_tbItemEMail');
    if (tb) {
        tb.value = data[0].strEMail;
        var nType = parseInt(data[0].strType);
        if (bUserAdmin == false || nType > 0)
            tb.disabled = true;
    }

    tb = document.getElementById('mainContent_tbItemDescription');
    if (tb)
        tb.value = data[0].strDescription;

    for (var i = 0; i < data[0].lstAccess.length; i++) {
        var nickName = data[0].lstAccess[i].strNickName;
    }

    showDialog('mainContent_mpeProperties');
}

function requestSaveProperties() {
    
    

    if (objPropertiesItem == null) return;
    // Ok l'objet json
    var tb = document.getElementById('mainContent_tbItemName');
    if (tb) objPropertiesItem[0].strName = tb.value;
    objPropertiesItem[0].strName = objPropertiesItem[0].strName.replace(/'/g, "\'");

    tb = document.getElementById('mainContent_tbItemEMail');
    if (tb) objPropertiesItem[0].strEMail = tb.value;

    tb = document.getElementById('mainContent_tbItemDescription');
    if (tb) objPropertiesItem[0].strDescription = tb.value;
    var des = objPropertiesItem[0].strDescription.replace("'", "&quote");
    objPropertiesItem[0].strDescription = objPropertiesItem[0].strDescription.replace(/'/g, "&quote");

    data: JSON.stringify(objPropertiesItem), //|-- Serialize
    closeDialog('mainContent_mpeProperties');
    

    $.ajax({
        type: "POST",
        url: "QualityFolder.aspx/requestSaveProperties",
        data: "{'Data': '" + strData + "'}",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: onRequestSavePropertiesOk,
        failure: onError
        
    });
}

function onRequestSavePropertiesOk(response) {
    if (response.d == null) return;
    if (response.d != "true") return;
    
    

    // Ok update element
    if (objLastSelected == null) return;
    var div = objLastSelected;
    var subTable = div.getElementsByTagName('TABLE')[0];
    // (row 0, col 1) = nom du dossier ou fichier
    subTable.rows[0].children[0].innerText = objPropertiesItem[0].strName;
 
    
    
}
    

//|---------------------------------------------
// Menu pour le droit d'accès
//|---------------------------------------------
var objSelectedUser = null;
function onMenuRight() {
    closePopupMenu();

    var hfSelectedType = document.getElementById('mainContent_hfSelectedType');
    if (hfSelectedType == null) return;
    var nType = parseInt(hfSelectedType.value);
    if (nType > 0) return; // On ne s'intéresse qu'aux dossier & dossier système

    // Masque le pdf si exist
    var iFrame = document.getElementById('ifDoc');
    if (iFrame != null) {
        if (iFrame.style.display != 'none') {
            iFrame.style.display = 'none';
            bMustRestorePDFViewer = true;
        }
    }

    var hfSelected = document.getElementById('mainContent_hfSelectedFile');
    if (hfSelected == null) return;

    $.ajax({
        type: "POST",
        url: "QualityFolder.aspx/requestAccessList",
        data: "{'strFullPath':'" + hfSelected.value + "'}",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: onRequestAccessRightOk,
        failure: onError
    });

}

function onRequestAccessRightOk(response) {
    if (response.d == null) return;

    // Ok on emplire les infor sur la Dlg droit d'accès
    var lbFolder = document.getElementById('mainContent_lbFolder');
    if (lbFolder) lbFolder.innerHTML = getSelectedItemName();

    var dlRight = document.getElementById('mainContent_dlRight');
    if (dlRight == null) return;

    // Remove old list before
    while (dlRight.rows.length) {
        dlRight.deleteRow(0);
    }
    objSelectedUser = null;

    // Remplir maintenant la liste des users qui ont le droit
    var data = response.d;
    for (var i = 0; i < data.length; i++) {

        var row = dlRight.insertRow(-1);
        var td = row.insertCell(0);
        //var div = createDiv('New Folder for Test');

        // Créer le div pour cette td
        var div = document.createElement('div');
        div.className = "divUserItem";
        div.innerHTML = data[i].strEmpNickName;

        // Add new function onClick !
        var fct = "onUserClicked(this);";
        div.onclick = new Function(fct);

        // Add le hidden field pour le emp_code
        var hfCode = document.createElement('input');
        hfCode.setAttribute('type', 'hidden');
        hfCode.setAttribute('name', 'hfCode_' + i);
        hfCode.setAttribute('value', data[i].strEmpCode);
        div.appendChild(hfCode);

        td.appendChild(div);
    }

    showDialog('mainContent_mpeRight');
}


function onUserClicked(selectedItem) {
    if (objSelectedUser != null) {
        objSelectedUser.className = 'divUserItem';
    }
    objSelectedUser = selectedItem;
    objSelectedUser.className = 'divUserItemSelected';
}

function requestAddUser() {

    // De quel user ?
    var ddlUser = document.getElementById('mainContent_ddlUser');
    if (ddlUser == null) return;
    if (ddlUser.selectedIndex < 0) return; // Pas de selection

    var strEmpCode = ddlUser.options[ddlUser.selectedIndex].value;
    var hfSelected = document.getElementById('mainContent_hfSelectedFile');
    if (hfSelected == null) return;

    $.ajax({
        type: "POST",
        url: "QualityFolder.aspx/requestAddUser",
        data: "{'strFullPath':'" + hfSelected.value + "', 'strEmpCode':'" + strEmpCode + "'}",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: onAddUserOk,
        failure: onError
    });
}

function onAddUserOk(response) {
    if (response.d == null) return;
    if (response.d != "true") return;

    // Ajote dans la liste
    var ddlUser = document.getElementById('mainContent_ddlUser');
    if (ddlUser == null) return;
    if (ddlUser.selectedIndex < 0) return; // Pas de selection

    // DataList
    var dlRight = document.getElementById('mainContent_dlRight');
    if (dlRight == null) return;

    var row = dlRight.insertRow(-1);
    var td = row.insertCell(0);

    // Créer le div pour cette td
    var div = document.createElement('div');
    div.className = "divUserItem";
    div.innerHTML = ddlUser.options[ddlUser.selectedIndex].innerText;

    // Add new function onClick !
    var fct = "onUserClicked(this);";
    div.onclick = new Function(fct);

    // Add le hidden field pour le emp_code
    var hfCode = document.createElement('input');
    hfCode.setAttribute('type', 'hidden');
    hfCode.setAttribute('name', 'hfCode_' + row.rowIndex);
    hfCode.setAttribute('value', ddlUser.options[ddlUser.selectedIndex].value);
    div.appendChild(hfCode);

    td.appendChild(div);

}


function requestDeleteUser() {
    if (objSelectedUser == null) return;

    var hfCode = objSelectedUser.getElementsByTagName('INPUT')[0];
    if (hfCode == null) return;

    var hfSelected = document.getElementById('mainContent_hfSelectedFile');
    if (hfSelected == null) return;


    $.ajax({
        type: "POST",
        url: "QualityFolder.aspx/requestDeleteUser",
        data: "{'strFullPath':'" + hfSelected.value + "', 'strEmpCode':'" + hfCode.value + "'}",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: onDeleteUserOk,
        failure: onError
    });
}

function onDeleteUserOk(response) {
    if (response.d == null) return;
    if (response.d != "true") return;

    if (objSelectedUser == null) return;
    // 1 Parent = td, 2 parent = tr
    var row = objSelectedUser.parentElement.parentElement;
    if (row == null) return;

    // DataList
    var dlRight = document.getElementById('mainContent_dlRight');
    if (dlRight == null) return;

    dlRight.deleteRow(row.rowIndex);

}



using Microsoft.Exchange.WebServices.Data;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.IO;
using System.Web;
using System.Web.Services;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Windows.Forms;
using System.Xml;


public class QualityAccess
{
    public string strEmpCode { get; set; }
    public string strNickName { get; set; }
    public string strEmail { get; set; }
}

public class QualityItem
{
    public string strFullPath { get; set; }
    public string strName { get; set; }
    public string strDescription { get; set; }

    public string strParent { get; set; }
    public string strType { get; set; }
    public string strEMail { get; set; }

    public List<QualityAccess> lstAccess;

    public QualityItem()
    {
        lstAccess = new List<QualityAccess>();
    }
}

public class QualityAccessRight
{
    public string strFullPath { get; set; }
    public string strEmpCode { get; set; }
    public string strEmpNickName { get; set; }

    public string strEmail { get; set; }
    
}


public partial class QualityFolder : System.Web.UI.Page
{
    public String m_strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
    String m_strEventArgument = "";
    String m_strPostBackCtrl = "";

    protected void Page_Load(object sender, EventArgs e)
    {
        m_strEventArgument = Request["__EVENTARGUMENT"];
        m_strPostBackCtrl = Request.Params.Get("__EVENTTARGET");


        if (IsPostBack == false)
        {

            //*--------------------------------------------------------------------------------
            // Init
            //*--------------------------------------------------------------------------------
            lbOpen.Attributes.Add("onclick", "document.getElementById('" + DocFileUpload.ClientID + "').click(); return false;");
            hfFolderTree.Value = "";
            FillUserList();

            //*--------------------------------------------------------------------------------
            // check argument
            //*--------------------------------------------------------------------------------
            if (Request.QueryString["folder"] == null)
                gotoFolder("");
            else
            {
                if (Request.QueryString["file"] != null)
                {
                    hfSelectedFile.Value = Request.QueryString["file"].ToString();
                    hfSelectedType.Value = GetFileType(hfSelectedFile.Value);
                }
                String strFullPath = Request.QueryString["folder"].ToString();
                gotoFolder(strFullPath);
            }

        }
        else
        {
            switch (m_strPostBackCtrl)
            {
                case "executeMenu":
                    executeMenu(m_strEventArgument);
                    break;
                case "gotoFolder":
                    gotoFolder(m_strEventArgument);
                    break;
                case "uploadFile":
                    UploadFile();
                    break;
                case "downloadFile":
                    DownloadFile(m_strEventArgument);
                    break;

                case "deleteFile":
                    DeleteFile(m_strEventArgument);
                    break;

                default:
                    break;
            }
        }
        
    



}

    protected String GetFileType(String strFile)
    {
        int nIndex = strFile.LastIndexOf('.');
        if (nIndex < 0) return "-1";
        string strExt = strFile.Substring(nIndex + 1).ToUpper();
        switch (strExt)
        {
            case "DOC":
            case "DOCX":
            case "DOTX":
                return "1"; // MSWORD

            case "XLS":
            case "XLSX":
                return "2"; // EXCEL

            case "PDF":
                return "3"; // PDF

            case "JPEG":
            case "JPG":
            case "GIF":
            case "BMP":
            case "PNG":
                return "4"; // Image

            case "PPT":
            case "PPTX":
                return "5"; // Power point

            case "ZIP":
            case "RAR":
                return "6"; // Fichier compressé

            case "MPP":
                return "7"; // Project

            case "XML":
                return "8"; // XML
            case "BIN":
                return "9"; // BIN
            case "DWG":
                return "10"; // BIN

            default:
                break;
        }

        return "-1";
    }


    protected void FillUserList()
    {
        DataTable dt = DBHelper.SQLOpen("select EMP_CODE, EMP_LAS_NAME, EMP_FIR_NAME from EMPLOYES where EMP_STATUS <> -1 and EMP_PW = 'NIDEK' order by EMP_NIC_NAME", m_strConnection);
        for (int i = 0; i < dt.Rows.Count; i++)
        {
            ListItem li = new ListItem();
            li.Text = DBHelper.GetString(i, "EMP_FIR_NAME", dt) + " " + DBHelper.GetString(i, "EMP_LAS_NAME", dt);
            li.Value = DBHelper.GetString(i, "EMP_CODE", dt);
            ddlUser.Items.Add(li);
        }

        //|------------------------------------------------------------------------------
        // Très important !
        // On ajoute une ligne vide pour que l'object soit visible dans javascript
        //|------------------------------------------------------------------------------
        DataTable dtUser = new DataTable();
        dtUser.TableName = "UserRightTable";
        dtUser.Columns.Add(new DataColumn("BD_EMP_CODE", typeof(string)));
        dtUser.Columns.Add(new DataColumn("EMP_FIR_NAME", typeof(string)));
        dtUser.Columns.Add(new DataColumn("EMP_LAS_NAME", typeof(string)));
        DataRow dr = dtUser.NewRow();
        dr[0] = "";
        dr[1] = "";
        dr[2] = "";
        dtUser.Rows.Add(dr);


        string strSQL = "select BD_EMP_CODE, EMP_FIR_NAME, EMP_LAS_NAME from BD_RIGHT inner join EMPLOYES on BD_RIGHT.BD_EMP_CODE = EMPLOYES.EMP_CODE";
        dt = DBHelper.SQLOpen(strSQL, m_strConnection);

        //dataTable1.Merge(dataTable2);
        dtUser.Merge(dt);
        dlRight.DataSource = dtUser;
        dlRight.DataBind();

    }

    public bool ShowMsgBox(String strMessage, String strTitle, String strAction)
    {
        lbMsgBoxTitle.Text = strTitle;
        lbMsgBoxMessage.Text = strMessage;
        hfMsgBoxAction.Value = strAction;
        MsgBoxUpdatePanel.Update();
        mpeMsgBox.Show();
        return true;
    }

    protected void gotoFolder(String strFullPath)
    {
        String strSQL = "";

        if (strFullPath == "")
            strSQL = "select * from BASE_DOCUMENTAIRES where BD_FOLDER_PARENT is NULL or BD_FOLDER_PARENT = '' or BD_FOLDER_PARENT = 'Quality' order by BD_TYPE, BD_ORDER";
        else
            strSQL = "select * from BASE_DOCUMENTAIRES where BD_FOLDER_PARENT = '" + strFullPath + "' order by BD_TYPE, BD_NAME ASC";

        hfFolderTree.Value = strFullPath;
        // Extract le dossier courant
        String strPath = strFullPath;
        int nIndex = strPath.LastIndexOf('/');
        if (nIndex >= 0)
            strPath = strPath.Substring(nIndex + 1);
        hfCurrentFolder.Value = strPath;

        DataTable dt = DBHelper.SQLOpen(strSQL, m_strConnection);
        dlFolder.DataSource = dt;
        dlFolder.DataBind();
        Session["BASE_DOCUMENTAIRES"] = dt;

    }

    protected void DeleteFile(String strFullPath)
    {
        String strPath = HttpContext.Current.Server.MapPath("Images\\") + strFullPath;
        try
        {
            FileInfo fi = new FileInfo(strPath);
            if (fi.Exists)
            {
                fi.Delete();
            }
        }
        catch (Exception ex)
        {
            ShowMsgBox(ex.Message, "SYSTEM ERROR", "MsgBox");
            return;
        }

        // Ok mise à jour dans la database
        String strSQL = "delete from  BASE_DOCUMENTAIRES where BD_FULL_PATH LIKE '" + strFullPath + "%'";
        if (DBHelper.SQLExecute(strSQL, m_strConnection) == false) return;
        String strParentFolder = hfFolderTree.Value;
        gotoFolder(strParentFolder);

    }

    protected void executeMenu(String strItem)
    {
        switch (strItem)
        {
            case "deleteFolder":
                ShowMsgBox("Vous allez supprimer le dossier " + hfCurrentFolder.Value + " et tout son contenu<br/>Voulez vous continuer ?", "ATTENTION !", "DeleteFolder");
                break;

            default:
                break;
        }
    }


    // Type de Item
    // -1 = Folder System
    // 0 = Folder
    // 1 = Doc
    // 2 = Xls
    // 3 = Pdf
    // 4 = Jpeg, jpg, png, bmp
    protected void dlFolder_ItemDataBound(object sender, DataListItemEventArgs e)
    {
        if (e.Item != null)
        {
            //HiddenField hfRowIndex = (HiddenField)e.Item.FindControl("hfRowIndex");
            //if (hfRowIndex == null) return;
            //hfRowIndex.Value = e.Item.ItemIndex.ToString();

            if (String.IsNullOrEmpty(hfSelectedFile.Value) == false)
            {
                String strFullPath = DataBinder.Eval(e.Item.DataItem, "BD_FULL_PATH").ToString();
                if (strFullPath == hfSelectedFile.Value)
                {
                    hfSelectedRow.Value = e.Item.ItemIndex.ToString();
                }
            }

            Image im = (Image)e.Item.FindControl("imgFileType");
            if (im == null) return;
            String strType = DataBinder.Eval(e.Item.DataItem, "BD_TYPE").ToString().ToUpper();
            switch (strType)
            {
                case "1":
                    im.ImageUrl = "~/Images/icon_word.png";
                    break;

                case "2":
                    im.ImageUrl = "~/Images/icon_excel.png";
                    break;

                case "3":
                    im.ImageUrl = "~/Images/icon_pdf.png";
                    break;

                case "4":
                    im.ImageUrl = "~/Images/icon_image.png";
                    break;

                case "5":
                    im.ImageUrl = "~/Images/icon_powerpoint.png";
                    break;

                case "6":
                    im.ImageUrl = "~/Images/icon_winrar.png";
                    break;

                case "7":
                    im.ImageUrl = "~/Images/icon_project.png";
                    break;
                case "8":
                    im.ImageUrl = "~/Images/icon_xml.png";
                    break;
                case "9":
                    im.ImageUrl = "~/Images/icon_bin.png";
                    break;
                case "10":
                    im.ImageUrl = "~/Images/icon_dwg.png";
                    break;

                default:
                    im.ImageUrl = "~/Images/icon_folder_documents.png";
                    break;
            }

        }
    }

    protected void DownloadFile(string strFileName)
    {
        String strFile = Server.MapPath("Images/") + strFileName.Replace('/', '\\');
        int nIndex = strFile.LastIndexOf('\\');
        String strName = Uri.EscapeDataString(strFile.Substring(nIndex + 1));

        //Response.Clear();
        //Response.ClearHeaders();
        //Response.ClearContent();
        //Response.AddHeader("Content-Disposition", "attachment; filename=\"" + strName + "\"");
        //Response.AddHeader("Content-Length", strName.Length.ToString());
        //Response.ContentType = "text/plain";
        //Response.Flush();
        //Response.TransmitFile(strFile);
        //Response.End();

        Response.Clear();
        Response.ClearHeaders();
        Response.ClearContent();
        //switch (hfSelectedType.Value)
        //{
        //    case "1":   // word
        //        Response.ContentType = "application/ms-word";
        //        break;
        //    case "2": // excel
        //        Response.ContentType = "application/pdf";
        //        break;
        //    default:
        //        Response.ContentType = "application/pdf";
        //        break;
        //}

        Response.AppendHeader("Content-Disposition", "attachment; filename=" + strName);

        // Write the file to the Response
        const int bufferLength = 10000;
        byte[] buffer = new Byte[bufferLength];
        int length = 0;
        Stream download = null;
        try
        {
            download = new FileStream(strFile, FileMode.Open, FileAccess.Read);
            Response.AddHeader("Content-Length", download.Length.ToString());
            Response.ContentType = "application/octet-stream";

            do
            {
                if (Response.IsClientConnected)
                {
                    length = download.Read(buffer, 0, bufferLength);
                    Response.OutputStream.Write(buffer, 0, length);
                    buffer = new Byte[bufferLength];
                }
                else
                {
                    length = -1;
                }
            }
            while (length > 0);
            Response.Flush();
            Response.End();
        }
        finally
        {
            if (download != null)
                download.Close();
        }

    }

    protected void UploadFile()
    {

        NKUser nkUser = (NKUser)Session["User"];
        if (nkUser.HasRight(NKUser.USER_RIGHT_UPLOAD) == false)
        {
            ShowMsgBox("Vous n'avez pas le droit pour cette opération", "ERREUR !", "MsgBox");
            return;
        }
        if (DocFileUpload.HasFile == false) return;

        // Save this file to temps folder
        String strDate = DBHelper.GetCurrentDate();
        String strParentFolder = hfFolderTree.Value;
        if (String.IsNullOrEmpty(strParentFolder))
            strParentFolder = "Quality";
        String strFullPath = strParentFolder + "/" + DocFileUpload.FileName.Replace("'", "&quote");
        String strFileName = Server.MapPath("Images/") + strFullPath;

        int nIndex = DocFileUpload.FileName.LastIndexOf('.');
        if (nIndex < 0) return;
        String strExt = DocFileUpload.FileName.Substring(nIndex + 1).ToUpper();
        String strDisplay = tbDisplay.Text;
        if (strDisplay == "")
            strDisplay = DocFileUpload.FileName.Substring(0, nIndex);
        String strDescription = tbDescription.Text;

        switch (strExt)
        {
            case "DOC":
            case "DOCX":
                nIndex = 1;
                break;

            case "XLS":
            case "XLSX":
                nIndex = 2;
                break;

            case "PDF":
                nIndex = 3;
                break;

            case "PNG":
            case "JPG":
            case "JPEG":
            case "GIF":
            case "BMP":
                nIndex = 4;
                break;

            case "PPT":
            case "PPTX":
                nIndex = 5;
                break;

            case "ZIP":
            case "RAR":
                nIndex = 6;
                break;

            case "MPP":
                nIndex = 7;
                break;

            case "XML":
                nIndex = 8;
                break;
            case "BIN":
                nIndex = 9;
                break;
            case "DWG":
                nIndex = 10;
                break;

            default:
                ShowMsgBox("Le type de fichier [" + strExt + "] n'est pas connu !\r\nLes fichiers valables sont Word, Pdf, Excel, Images, Zip.", "ERREUR !", "MsgBox");
                return;

        }

        try
        {
            FileInfo fi = new FileInfo(strFileName);
            if (fi.Exists)
            {
                fi.Delete();
            }
            DocFileUpload.SaveAs(strFileName);

            // Save ok, insert into database
            String strParent = strParentFolder;
            //if (strParent == "Quality") strParent = ""; // Suis à la racine !
            int nOrder = 0;
            DataTable dt;
            if (strParent == "")
                dt = DBHelper.SQLOpen("select BD_ORDER from BASE_DOCUMENTAIRES " +
                    "where BD_FOLDER_PARENT is NULL or BD_FOLDER_PARENT = '' or BD_FOLDER_PARENT = 'Quality' order by BD_ORDER desc", m_strConnection);
            else
                dt = DBHelper.SQLOpen("select BD_ORDER from BASE_DOCUMENTAIRES where BD_FOLDER_PARENT = '" + strParent + "' order by BD_ORDER desc", m_strConnection);

            if (dt.Rows.Count > 0)
                nOrder = DBHelper.GetInt(0, "BD_ORDER", dt) + 1;


            String strSQL = "insert into BASE_DOCUMENTAIRES" +
                "(BD_FULL_PATH, BD_NAME, BD_DESCRIPTION, BD_FOLDER_PARENT, BD_CREATE_DATE, BD_CREATE_USER, BD_MODIFY_DATE, BD_MODIFY_USER, BD_TYPE, BD_ORDER) values (" +
                "'" + DBHelper.CharToSQL(strFullPath) + "', '" + DBHelper.CharToSQL(strDisplay) + "', '" + DBHelper.CharToSQL(strDescription) + "', '" + strParent + "', " +
                strDate + ", '" + nkUser.m_strCode + "', NULL, NULL, " + nIndex.ToString() + ", " + nOrder.ToString() + ")";
            if (DBHelper.SQLExecute(strSQL, m_strConnection) == false)
            {
                fi.Delete();
                ShowMsgBox("System error please contact admin for more details !", "ERROR !", "MsgBox");
                return;
            }
            string strTo = "othmaneqlq@gmail.com";
            string strObject = "Nouveau document ajouté";
            string strBody = "Un nouveau document a été ajouté.\r\n\r\n" + "Nom : " + strDisplay + "\r\n" + "Ajouté par : " + nkUser.m_strCode + "\r\n" + "Dossier : " + strParentFolder;
           string result = QualityFolder.sendRequestModification(strTo, strObject, strBody, strFullPath);

            if (result != "true")
            {
                ShowMsgBox("Fichier ajouté, mais erreur envoi mail : " + result, "AVERTISSEMENT", "MsgBox");
            }
      

            // Reload...
            gotoFolder(strParentFolder);
            //if(dlFolder.Items.Count <= 0)
            //{
            //    hfAddNewFile.Value = "";
            //    gotoFolder(strParentFolder);
            //}
            //else
            //    hfAddNewFile.Value = strDisplay + "/" + nIndex.ToString() + "/" + strFullPath;
        }
        catch (Exception ex)
        {
            ShowMsgBox(ex.Message, "ERREUR SYSTEME !", "MsgBox");
        }

    }


    //|--------------------------------------------------------------
    // Les WEB Services
    //|--------------------------------------------------------------
    [WebMethod(EnableSession = true)]
    public static string addNewFolder(string strParentFolder, string strFolder, string strDisplay, string strDescription)
    {
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        if (HttpContext.Current.Session["User"] == null) return "ERROR, no login user !";
        NKUser nkUser = (NKUser)HttpContext.Current.Session["User"];

        if (String.IsNullOrEmpty(strParentFolder))
            return "ERROR ! Parent Folder name is empty !";

        if (String.IsNullOrEmpty(strFolder))
            return "ERROR ! Folder name is empty !";

        // Check if this folder already exist !
        //String strPath = "~/Images/" + strParentFolder + "/" + strFolder;
        String strPath = HttpContext.Current.Server.MapPath("Images/") + strParentFolder + "\\" + strFolder;

        if (NKTools.IsFolderExist(strPath))
            return "This folder is already exist !";

        // Ok créer maintenant
        try
        {
            Directory.CreateDirectory(strPath);
        }
        catch (Exception ex)
        {
            return "ERROR : " + ex.Message;
        }

        String strParent = strParentFolder;
        if (strParent == "Quality") strParent = ""; // Suis à la racine !
        int nOrder = 0;
        DataTable dt;
        if (strParent == "")
            dt = DBHelper.SQLOpen("select BD_ORDER from BASE_DOCUMENTAIRES where BD_FOLDER_PARENT is NULL or BD_FOLDER_PARENT = '' order by BD_ORDER desc", strConnection);
        else
            dt = DBHelper.SQLOpen("select BD_ORDER from BASE_DOCUMENTAIRES where BD_FOLDER_PARENT = '" + strParent + "' order by BD_ORDER desc", strConnection);

        if (dt.Rows.Count > 0)
            nOrder = DBHelper.GetInt(0, "BD_ORDER", dt) + 1;

        // Ok mise à jour dans la database

        strPath = strParentFolder + "/" + strFolder;
        String strDate = DBHelper.GetCurrentDate();
        String strSQL = "insert into BASE_DOCUMENTAIRES" +
            "(BD_FULL_PATH, BD_NAME, BD_DESCRIPTION, BD_FOLDER_PARENT, BD_CREATE_DATE, BD_CREATE_USER, BD_MODIFY_DATE, BD_MODIFY_USER, BD_TYPE, BD_ORDER) values (" +
            "'" + strPath + "', '" + DBHelper.CharToSQL(strDisplay) + "', '" + DBHelper.CharToSQL(strDescription) + "', '" + strParent + "', " +
            strDate + ", '" + nkUser.m_strCode + "', NULL, NULL, 0, " + nOrder.ToString() + ")";
        if (DBHelper.SQLExecute(strSQL, strConnection) == false)
        {
            Directory.Delete(strPath);
            return "System error please contact admin for more details !";
        }

        return "true";
    }
    public static string DeleteDir(string strFolder)
    {
        String strRetCode;
        String strSQL;
        int nIndex;
        String strFullPath;
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        try
        {
            DirectoryInfo dir = new DirectoryInfo(strFolder);
            foreach (FileInfo fi in dir.GetFiles())
            {
                // Ok mise à jour dans la database        
                nIndex = fi.FullName.IndexOf("\\Quality");
                strFullPath = fi.FullName.Substring(nIndex + 1);
                strFullPath = strFullPath.Replace('\\', '/');
                strSQL = "delete from  BASE_DOCUMENTAIRES where BD_FULL_PATH = '" + strFullPath + "'";
                if (DBHelper.SQLExecute(strSQL, strConnection) == false)
                    return "System error please contact admin for more details !";

                fi.Delete();


            }
            foreach (DirectoryInfo di in dir.GetDirectories())
            {
                strRetCode = DeleteDir(di.FullName);
                if (strRetCode != "true") return strRetCode;
            }

            // Finalement
            dir.Delete();

        }
        catch (Exception ex)
        {
            return "ERROR : " + ex.Message;
        }

        // Ok mise à jour dans la database
        nIndex = strFolder.IndexOf("\\Quality");
        strFullPath = strFolder.Substring(nIndex + 1);
        strFullPath = strFullPath.Replace('\\', '/');
        strSQL = "delete from  BASE_DOCUMENTAIRES where BD_FULL_PATH = '" + strFullPath + "'";
        if (DBHelper.SQLExecute(strSQL, strConnection) == false)
            return "System error please contact admin for more details !";

        return "true";
    }

    [WebMethod(EnableSession = true)]
    public static string deleteFolder(string strFullPath)
    {
        String strPath = HttpContext.Current.Server.MapPath("Images\\") + strFullPath;
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        if (Directory.Exists(strPath))
        {
            DBHelper.SQLExecute("delete from BASE_DOCUMENTAIRES where BD_FULL_PATH LIKE '" + strFullPath + "'", strConnection);
            return "true";
        }

            return DeleteDir(strPath);
    }


    //|---------------------------------------------------------
    // This run only one shoot, cause index not re sort !
    // Abandon, mais keep this in mind, maybe userful later
    //|---------------------------------------------------------
    [WebMethod(EnableSession = true)]
    public static string deleteFile(string strFullPath)
    {

        String strPath = HttpContext.Current.Server.MapPath("Images\\") + strFullPath;
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        try
        {
            FileInfo fi = new FileInfo(strPath);
            if (fi.Exists)
            {
                fi.Delete();
            }
        }
        catch (Exception ex)
        {
            return "ERROR : " + ex.Message;
        }

        // Ok mise à jour dans la database
        String strSQL = "delete from  BASE_DOCUMENTAIRES where BD_FULL_PATH = '" + strFullPath + "'";
        if (DBHelper.SQLExecute(strSQL, strConnection) == false)
            return "System error please contact admin for more details !";

        return "true";
    }

    [WebMethod(EnableSession = true)]
    public static string moveLineUp(string strLineNumber)
    {
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        if (HttpContext.Current.Session["BASE_DOCUMENTAIRES"] == null) return "ERROR ! Session variable not found !";
        DataTable dt = (DataTable)HttpContext.Current.Session["BASE_DOCUMENTAIRES"];

        String strOriginValue, strTargetValue;
        int nOriginOrder, nTargetOrder;

        int nLineNumber = -1;
        int.TryParse(strLineNumber, out nLineNumber);

        if (nLineNumber < 0 || nLineNumber >= dt.Rows.Count)
            return "false";

        if (nLineNumber == 0 || dt.Rows.Count == 1)
            return "";

        strOriginValue = DBHelper.GetString(nLineNumber, "BD_FULL_PATH", dt);
        nOriginOrder = DBHelper.GetInt(nLineNumber, "BD_ORDER", dt);

        strTargetValue = DBHelper.GetString(nLineNumber - 1, "BD_FULL_PATH", dt);
        nTargetOrder = DBHelper.GetInt(nLineNumber - 1, "BD_ORDER", dt);

        // Swap
        dt.Rows[nLineNumber]["BD_ORDER"] = nTargetOrder;
        dt.Rows[nLineNumber - 1]["BD_ORDER"] = nOriginOrder;
        DataRow tempRow = dt.NewRow();
        for (int i = 0; i < dt.Columns.Count; i++)
        {
            tempRow[i] = dt.Rows[nLineNumber][i];
            dt.Rows[nLineNumber][i] = dt.Rows[nLineNumber - 1][i];
            dt.Rows[nLineNumber - 1][i] = tempRow[i];
        }

        HttpContext.Current.Session["BASE_DOCUMENTAIRES"] = dt;

        // Ok update now database
        String strSQL = "update BASE_DOCUMENTAIRES set BD_ORDER = " + nTargetOrder.ToString() + " where BD_FULL_PATH = '" + strOriginValue + "'";
        if (DBHelper.SQLExecute(strSQL, strConnection) == false)
            return "System error please contact admin for more details !";
        strSQL = "update BASE_DOCUMENTAIRES set BD_ORDER = " + nOriginOrder.ToString() + " where BD_FULL_PATH = '" + strTargetValue + "'";
        if (DBHelper.SQLExecute(strSQL, strConnection) == false)
            return "System error please contact admin for more details !";

        return "true";
    }

    [WebMethod(EnableSession = true)]
    public static string moveLineDown(string strLineNumber)
    {
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        if (HttpContext.Current.Session["BASE_DOCUMENTAIRES"] == null) return "ERROR ! Session variable not found !";
        DataTable dt = (DataTable)HttpContext.Current.Session["BASE_DOCUMENTAIRES"];

        String strOriginValue, strTargetValue;
        int nOriginOrder, nTargetOrder;

        int nLineNumber = -1;
        int.TryParse(strLineNumber, out nLineNumber);

        if (nLineNumber < 0 || nLineNumber >= dt.Rows.Count)
            return "false";

        if (nLineNumber == dt.Rows.Count - 1 || dt.Rows.Count == 1)
            return "";

        strOriginValue = DBHelper.GetString(nLineNumber, "BD_FULL_PATH", dt);
        nOriginOrder = DBHelper.GetInt(nLineNumber, "BD_ORDER", dt);

        strTargetValue = DBHelper.GetString(nLineNumber + 1, "BD_FULL_PATH", dt);
        nTargetOrder = DBHelper.GetInt(nLineNumber + 1, "BD_ORDER", dt);

        // Swap
        dt.Rows[nLineNumber]["BD_ORDER"] = nTargetOrder;
        dt.Rows[nLineNumber + 1]["BD_ORDER"] = nOriginOrder;
        DataRow tempRow = dt.NewRow();
        for (int i = 0; i < dt.Columns.Count; i++)
        {
            tempRow[i] = dt.Rows[nLineNumber][i];
            dt.Rows[nLineNumber][i] = dt.Rows[nLineNumber + 1][i];
            dt.Rows[nLineNumber + 1][i] = tempRow[i];
        }

        HttpContext.Current.Session["BASE_DOCUMENTAIRES"] = dt;

        // Ok update now database
        String strSQL = "update BASE_DOCUMENTAIRES set BD_ORDER = " + nTargetOrder.ToString() + " where BD_FULL_PATH = '" + strOriginValue + "'";
        if (DBHelper.SQLExecute(strSQL, strConnection) == false)
            return "System error please contact admin for more details !";
        strSQL = "update BASE_DOCUMENTAIRES set BD_ORDER = " + nOriginOrder.ToString() + " where BD_FULL_PATH = '" + strTargetValue + "'";
        if (DBHelper.SQLExecute(strSQL, strConnection) == false)
            return "System error please contact admin for more details !";

        return "true";
    }

    [WebMethod(EnableSession = true)]
    public static string requestFolderInformation(String strFullPath)
    {
        if (HttpContext.Current.Session["User"] == null) return "ERROR ! User not login !";
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;

        // Cherche le REP parent !
        String strParent = "";
        int nIndex = strFullPath.IndexOf('/', 9); // 9 car on ignore le 'Quality/'
        if (nIndex < 0) strParent = strFullPath;
        else
        {
            strParent = strFullPath.Substring(0, nIndex);
        }

        DataTable dt = DBHelper.SQLOpen("select * from BD_EMAIL where BD_FULL_PATH = '" + strParent + "'", strConnection);
        if (dt.Rows.Count <= 0) return "ERROR ! No email associate for this folder !";
        NKUser nkUser = (NKUser)HttpContext.Current.Session["User"];
        return DBHelper.GetString(0, "BD_EMAIL", dt) + "/" + nkUser.m_strEMail;
    }

    [WebMethod(EnableSession = true)]
    public static string sendRequestModification(string strTo, string strObject, string strBody, string strAttach)
    {
        String strUser, strPW, strServer, strAuthenfification, strXmlFile, strAttachFile, strEMail;
        XmlDocument xmlFile = new XmlDocument();
        XmlNodeList xlmItems;

        if (HttpContext.Current.Session["User"] == null) return "ERROR ! User not login !";
        NKUser nkUser = (NKUser)HttpContext.Current.Session["User"];
        strXmlFile = HttpContext.Current.Server.MapPath("App_Data") + "\\Users\\" + nkUser.m_strCode + "\\SmtpServer.xml";
        if (NKTools.IsFileExist(strXmlFile) == false) return "ERROR ! Configuration file for email not found ";
        xmlFile.Load(strXmlFile);

        xlmItems = xmlFile.GetElementsByTagName("Authentification");
        strAuthenfification = xlmItems[0].InnerText.ToString().ToUpper();

        xlmItems = xmlFile.GetElementsByTagName("User");
        strUser = xlmItems[0].InnerText.ToString();

        xlmItems = xmlFile.GetElementsByTagName("MW");
        strPW = xlmItems[0].InnerText.ToString();

        xlmItems = xmlFile.GetElementsByTagName("SMTP");
        strServer = xlmItems[0].InnerText.ToString();


        // Reformatter...
        String strText = strBody.Replace('|', '\'');


        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
        service.Credentials = new WebCredentials(strUser, strPW);
        service.Url = new Uri( "https://mail.nidek.fr/EWS/Exchange.asmx" );
        service.Url = new Uri(strServer);
        EmailMessage message = new EmailMessage(service);
        message.Subject = strObject;
        //message.Body = MessageTextBox.Text;
        message.Body = strText.Replace("\r\n", "<br/>");

        //|*******************************************************************
        //|-- Multi destiny
        int nOffset = 0;
        int nIndex = strTo.IndexOf(';');
        while (nIndex > 0)
        {
            strEMail = strTo.Substring(nOffset, nIndex - nOffset).Trim();
            if (String.IsNullOrEmpty(strEMail) == false)
                message.ToRecipients.Add(strEMail);

            nOffset += (nIndex - nOffset) + 1;
            nIndex = strTo.IndexOf(';', nOffset);
        }
        // Last item;
        strEMail = strTo.Substring(nOffset).Trim();
        if (String.IsNullOrEmpty(strEMail) == false)
            message.ToRecipients.Add(strEMail);
        //|*******************************************************************

        // Cc tjs à audrey
        String strCC = ConfigurationManager.AppSettings["QualityManager"];
        if (String.IsNullOrEmpty(strCC) == false)
        {
            message.CcRecipients.Add(strCC);
        }

        // Attachment
        strAttachFile = HttpContext.Current.Server.MapPath("Images\\") + strAttach;
        strAttachFile = strAttachFile.Replace("/", "\\");
        if (NKTools.IsFileExist(strAttachFile) == false)
            return "ERROR ! Attach file not exist";

        message.Attachments.AddFileAttachment(strAttachFile);
        try
        {
            message.Save();
            message.SendAndSaveCopy();
        }
        catch (Exception ex)
        {
            return "ERROR : " + ex.ToString();
        }

        return "true";
    }

    [WebMethod(EnableSession = true)]
    public static string loadUserRight(string strFullPath)
    {
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        NKUser nkUser = (NKUser)HttpContext.Current.Session["User"];
        if (nkUser != null && nkUser.HasRight(NKUser.USER_RIGHT_QUALITY))
        {
            return "admin";
        }

        if (String.IsNullOrEmpty(strFullPath)) return "false";

        // Sinon on décompose le full path
        List<string> lstPath = new List<string>();
        int nIndex = strFullPath.IndexOf('/');
        if (nIndex < 0) return "false"; // Nothing to do...
        String strData, strPath = strFullPath.Substring(nIndex + 1);
        strData = "Quality";
        nIndex = strPath.IndexOf('/');
        while (nIndex != -1)
        {
            strData += "/" + strPath.Substring(0, nIndex);
            lstPath.Add(strData);
            strPath = strPath.Substring(nIndex + 1);
            nIndex = strPath.IndexOf('/');
        }
        // Last element
        strData += "/" + strPath;
        lstPath.Add(strData);

        // Construire le SQL
        String strSQL = "select * from BD_RIGHT where BD_EMP_CODE = '" + nkUser.m_strCode + "' and (";
        nIndex = 0;
        foreach (String strItem in lstPath)
        {
            if (nIndex == 0)
            {
                strSQL += "BD_FULL_PATH = '" + strItem + "'";
                nIndex = 1;
            }
            else
            {
                strSQL += " or BD_FULL_PATH = '" + strItem + "'";
            }
        }
        strSQL += ")";

        DataTable dt = DBHelper.SQLOpen(strSQL, strConnection);
        if (dt.Rows.Count > 0)
            return "true";

        return "false";

    }

    [WebMethod(EnableSession = true)]
    public static List<QualityItem> requestProperties(string strFullPath)
    {
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        List<QualityItem> lstItem = new List<QualityItem>();

        String strSQL = "select BASE_DOCUMENTAIRES.*, BD_EMAIL.BD_EMAIL from BASE_DOCUMENTAIRES left join BD_EMAIL on BASE_DOCUMENTAIRES.BD_FULL_PATH = BD_EMAIL.BD_FULL_PATH " +
            "where BASE_DOCUMENTAIRES.BD_FULL_PATH = '" + strFullPath + "'";

        DataTable dt = DBHelper.SQLOpen(strSQL, strConnection);

        if (dt.Rows.Count > 0)
        {
            QualityItem item = new QualityItem();
            item.strFullPath = strFullPath;
            item.strName = DBHelper.GetString(0, "BD_NAME", dt);
            item.strDescription = DBHelper.GetString(0, "BD_DESCRIPTION", dt);

            item.strParent = DBHelper.GetString(0, "BD_FOLDER_PARENT", dt);
            item.strType = DBHelper.GetAbsoluteString(0, "BD_TYPE", dt);
            item.strEMail = DBHelper.GetString(0, "BD_EMAIL", dt);

            // Mantenant liste des droit
            strSQL = "select BD_RIGHT.*, EMPLOYES.EMP_NIC_NAME, EMPLOYES.EMP_EMAIL from BD_RIGHT inner join EMPLOYES on BD_RIGHT.BD_EMP_CODE = EMPLOYES.EMP_CODE " +
            "where BD_RIGHT.BD_FULL_PATH = '" + strFullPath + "'";
            dt = DBHelper.SQLOpen(strSQL, strConnection);
            for (int i = 0; i < dt.Rows.Count; i++)
            {
                QualityAccess qa = new QualityAccess();
                qa.strEmpCode = DBHelper.GetString(i, "BD_EMP_CODE", dt);
                qa.strNickName = DBHelper.GetString(i, "EMP_NIC_NAME", dt);
                qa.strEmail = DBHelper.GetString(i, "EMP_EMAIL", dt);
                item.lstAccess.Add(qa);
            }

            lstItem.Add(item);
        }

        return lstItem;
    }

    [WebMethod(EnableSession = true)]
    public static string requestSaveProperties(string strData)
    {
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        List<QualityItem> lstItem = JsonConvert.DeserializeObject<List<QualityItem>>(strData);

        String strName = lstItem[0].strName.Replace("&quote", "'");
        String strDescription = lstItem[0].strDescription.Replace("&quote", "'");


        String strSQL = "update BASE_DOCUMENTAIRES set BD_NAME = '" + DBHelper.CharToSQL(strName)
            + "', BD_DESCRIPTION = '" + DBHelper.CharToSQL(strDescription) + "' where BD_FULL_PATH = '" + lstItem[0].strFullPath + "'";


        if (DBHelper.SQLExecute(strSQL, strConnection) == false) return "false";


        strSQL = "UPDATE BASE_DOCUMENTAIRES "
        + "SET BD_FULL_PATH = REPLACE(BD_FULL_PATH, '" + lstItem[0].strFullPath + "', '" + lstItem[0].strParent + "/" + lstItem[0].strName + "') "
        + "where BD_FULL_PATH like '" + lstItem[0].strFullPath  + "'";

        if (DBHelper.SQLExecute(strSQL, strConnection) == false) return "false";

        strSQL = "UPDATE BASE_DOCUMENTAIRES "
        + "SET BD_FOLDER_PARENT = REPLACE(BD_FOLDER_PARENT, '" + lstItem[0].strFullPath + "', '" + lstItem[0].strParent + "/" + lstItem[0].strName + "') "
        + "where BD_FULL_PATH like '" + lstItem[0].strParent + "/" + lstItem[0].strName + "'";

        if (DBHelper.SQLExecute(strSQL, strConnection) == false) return "false";



        String strPath = ConfigurationManager.AppSettings["PATH"];

        //string strSource = "C:/inetpub/wwwroot/Quality/Images/" + lstItem[0].strFullPath;
        //string strDestination = "C:/inetpub/wwwroot/Quality/Images/" + lstItem[0].strParent + "/" + lstItem[0].strName;

        string strSource = strPath + lstItem[0].strFullPath;
        string strDestination = strPath + lstItem[0].strParent + '/' + lstItem[0].strName;
       
        try
        {
            File.Move(strSource, strDestination);
        

        }
        catch (Exception e)
        {
            Console.WriteLine(e.Message);
        }
        // Update aussi l'email de notification ssi droit Ok
        NKUser nkUser = (NKUser)HttpContext.Current.Session["User"];
        if (nkUser.HasRight(NKUser.USER_RIGHT_QUALITY))
        {
            strSQL = "update BD_EMAIL set BD_EMAIL.BD_EMAIL = '" + lstItem[0].strEMail
                        + "' where BD_FULL_PATH = '" + lstItem[0].strFullPath + "'";

           if (DBHelper.SQLExecute(strSQL, strConnection) == false) return "false";
        }

        return "true";
    }

    [WebMethod]
    public static List<QualityAccessRight> requestAccessList(string strFullPath)
    {
        List<QualityAccessRight> lstItem = new List<QualityAccessRight>();
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        DataTable dt = DBHelper.SQLOpen("select BD_RIGHT.*, EMPLOYES.EMP_FIR_NAME, EMPLOYES.EMP_LAS_NAME from BD_RIGHT inner join EMPLOYES on BD_RIGHT.BD_EMP_CODE = EMPLOYES.EMP_CODE " +
            "where BD_FULL_PATH = '" + strFullPath + "'", strConnection);

        for (int i = 0; i < dt.Rows.Count; i++)
        {
            QualityAccessRight item = new QualityAccessRight();
            item.strFullPath = strFullPath;
            item.strEmpCode = DBHelper.GetString(i, "BD_EMP_CODE", dt);
            item.strEmpNickName = DBHelper.GetString(i, "EMP_FIR_NAME", dt) + " " + DBHelper.GetString(i, "EMP_LAS_NAME", dt);
            lstItem.Add(item);
        }

        return lstItem;
    }

    [WebMethod]
    public static string requestAddUser(string strFullPath, string strEmpCode)
    {
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        // User exist ?
        DataTable dt = DBHelper.SQLOpen("select * from BD_RIGHT where BD_FULL_PATH = '" + strFullPath +
            "' and BD_EMP_CODE = '" + strEmpCode + "'", strConnection);
        if (dt.Rows.Count > 0) return "false";

        // Ok, add to data base
        if (DBHelper.SQLExecute("insert into BD_RIGHT(BD_FULL_PATH, BD_EMP_CODE, BD_RIGHT) values (" +
            "'" + strFullPath + "', '" + strEmpCode + "', 65535 )", strConnection) == false)
            return "false";

        return "true";
    }

    [WebMethod]
    public static string requestDeleteUser(string strFullPath, string strEmpCode)
    {
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;

        // Ok, add to data base
        if (DBHelper.SQLExecute("delete from BD_RIGHT where BD_FULL_PATH = '" + strFullPath + "' and BD_EMP_CODE = '" +
            strEmpCode + "'", strConnection) == false)
            return "false";

        return "true";
    }


}

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
            if (name == "ACH")
                return " Achat";
            if (name == "MKT")
                return " Marketing";
            if (name == "ADV")
                return " Administration Des Ventes";
            if (name == "AUT")
                return " Autre";
            if (name == "DAF")
                return "Finances";
            if (name == "DRH")
                return "Ressources Humaines";
            if (name == "GEN")
                return " Management";
            if (name == "INF")
                return " Informatique";
            if (name == "LOG")
                return " Logistique";
            if (name == "TAB")
                return " Site de fabrication";
            if (name == "TEC")
                return " Technique";
            if (name == "VEN")
                return " Vente";
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
                a.value ="";
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
                    iframe.src = "https://docs.google.com/gview?url=http://nidek-w2k8-mob.nidek.fr:5004/Images/" + encodeURI(strFullPath) + "&embedded=true";
                    //iframe.src = "Images/" + encodeURI(strFullPath) ;



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
                <h2>Aborescence :</h2>
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
                <asp:UpdatePanel ID="upFiles" runat="server" UpdateMode="Conditional" ChildrenAsTriggers="true">
                    <ContentTemplate>
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
                    </ContentTemplate>
                </asp:UpdatePanel>
            </td>
            <%--Colonne pour la vue du document--%>
            <td style="width: 1050px; height: 700px; vertical-align: top; border: 0.5px solid #b6b6b6;">
                <%--Pour affciher un PDF ou word, Excel...--%>
                <iframe id="ifDoc"
                    style="width: 1050px; height: 700px; display: none;"
                    onload="hideInfoDlg();"></iframe>
                <%--Pour affciher une image (jpeg, png, bmp, etc...--%>
                <div id="divImageViewer" style="width: 1050px; height: 700px; display: none;"></div>
            </td>
        </tr>
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

            <asp:FileUpload ID="DocFileUpload" runat="server" Style="display: none" onchange="onFileSelected();" />
            <table style="width: 100%;">
                <tr>
    <td style="width: 100px;">Destinataires :</td>
    <td style="width: 550px;">
        <asp:TextBox ID="TextBox1" runat="server" Width="100%"></asp:TextBox>
    </td>
</tr>
<tr>
    <td>Objet :</td>
    <td>
        <asp:TextBox ID="TextBox2" runat="server" Width="100%"></asp:TextBox>
    </td>
</tr>
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
                </tr>
                <tr style="height:150px;">
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
                    <td style="width: 100px;">Dosier :</td>
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




</asp:Content>

