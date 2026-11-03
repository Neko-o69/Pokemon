switch (nType) {
    //|------------------------------------------------------
    // Word ou Excel, ou PowerPoint
    //|------------------------------------------------------
    case 1: // Word
    case 2: // Excel
    case 5: // PowerPoint
    case 8: // XML

        divImageViewer.style.display = 'none';
        iframe.style.display = 'none';

        if (obj != null) {
            objLastSelected = obj;
            obj.className = "linkButtonQualityItemSelected";
        }

        hfSelected.value = strFullPath;

        showInfoDlg();
        window.open("/Images/" + strFullPath, '_blank');

        break;
