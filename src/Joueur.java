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
