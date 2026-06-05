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
 + "where BD_FULL_PATH like '" + lstItem[0].strFullPath + "'";

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
        File.Exists(strSource);
        File.Move(strSource, strDestination);
    }
    catch (Exception e)
    {
        return "false : " + e.Message;
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
    objPropertiesItem[0].strName = objPropertiesItem[0].strName.replace(/'/g, "\u2019");

    tb = document.getElementById('mainContent_tbItemEMail');
    if (tb) objPropertiesItem[0].strEMail = tb.value;

    tb = document.getElementById('mainContent_tbItemDescription');
    if (tb) objPropertiesItem[0].strDescription = tb.value;
    var des = objPropertiesItem[0].strDescription.replace(/'/g, "\u2019");
    objPropertiesItem[0].strDescription = objPropertiesItem[0].strDescription.replace(/'/g, "\u2019");

    var strData =
        JSON.stringify(objPropertiesItem); //|-- Serialize
    closeDialog('mainContent_mpeProperties');
    
    alert(strData)
    $.ajax({
        type: "POST",
        url: "QualityFolder.aspx/requestSaveProperties",
        data: JSON.stringify({strData: strData}),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: onRequestSavePropertiesOk,
        failure: onError
        
    });
}

function onRequestSavePropertiesOk(response) {
    if (response.d == null) return;
    if (response.d != "true") return;
    location.reload();
    
    

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

function InitDragAndDrop() {

    $("#dropZone").on("dragover", function (e) {
        e.preventDefault();
        $(this).css("background", "grey");
    });

    $("#dropZone").on("dragleave", function () {
        $(this).css("background", "white");
    });

    $("#dropZone").on("drop", function (e) {
        e.preventDefault();
        $(this).css("background", "white");

        var Files = e.originalEvent.dataTransfer.files;
        if (Files.length == 0) return;

        var monInput = document.getElementById('mainContent_DocFileUpload');
        var transfer = new DataTransfer();
        transfer.items.add(Files[0]);
        monInput.files = transfer.files;

        document.getElementById('mainContent_tbFileName').value = Files[0].name;

        checkAndUploadFile();
    });
}

