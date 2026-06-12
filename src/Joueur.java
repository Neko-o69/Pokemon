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
    if (name == "DOC")
        return " Documents";
    return name;
    if (name == "FORM")
        return " Formulaires";
    return name;
    if (name == "MOP")
        return " Mode Opératoire";
    return name;
    if (name == "PROC")
        return " Procédure";
    return name;
    if (name == "REF")
        return " Référence";
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
