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
