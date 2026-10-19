function doDrop(e, strFullPath, nType) {
    e.preventDefault();
    e.stopPropagation();

    var strSource = e.dataTransfer.getData("text");

    if (strSource == "")
        return false;

    if (nType != 0)
        return false;

    $.ajax({
        type: "POST",
        url: "QualityFolder.aspx/moveItem",
        data: JSON.stringify({
            strSource: strSource,
            strTarget: strFullPath
        }),
        contentType: "application/json; charset=utf-8",
        dataType: "json",

        success: function (response) {
            if (response.d == "true") {
                location.reload();
            }
            else {
                showMsgBox(response.d, "ATTENTION !", "MsgBox");
            }
        },

        error: function (xhr) {
            showMsgBox("Erreur lors du déplacement du fichier.", "ERREUR !", "MsgBox");
        }
    });

    return false;
}
function createQueryDiv(strTitle, strDescription, nType, strFullPath) {
    var div = document.createElement('div');

    div.className = "rbSearchQualityItem";

    // Permet de déplacer le fichier
    div.draggable = true;

    div.ondragstart = function (e) {
        e.dataTransfer.setData("text", strFullPath);
    };

    var fct = "onItemClicked(" + nType + ", '" + strFullPath + "');";
    div.onclick = new Function(fct);

    var table = createQueryTable(strTitle, strDescription, nType);
    div.appendChild(table);

    return div;
}
