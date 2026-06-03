// Bloquer le comportement par défaut sur toute la page
$(document).on('dragover drop', function(e) {
    e.preventDefault();
    e.stopPropagation();
});

var zone = document.getElementById('dropZone');
if (!zone) return;

zone.ondragover = function(e) {
    e.preventDefault();
    zone.style.background = '#cce';
    zone.style.borderColor = '#003399';
}

zone.ondragleave = function() {
    zone.style.background = '#f9f9f9';
    zone.style.borderColor = 'grey';
}

zone.ondrop = function(e) {
    e.preventDefault();
    zone.style.background = '#f9f9f9';
    zone.style.borderColor = 'grey';

    var fichier = e.dataTransfer.files[0];
    if (!fichier) return;

    var hfFolder = document.getElementById('mainContent_hfFolderTree');

    var formData = new FormData();
    formData.append('DocFileUpload', fichier);
    formData.append('__EVENTTARGET', 'uploadFileDrop');
    formData.append('__EVENTARGUMENT', '');
    formData.append('mainContent_hfFolderTree', hfFolder ? hfFolder.value : 'Quality');

    $.ajax({
        type: "POST",
        url: "QualityFolder.aspx",
        data: formData,
        contentType: false,
        processData: false,
        success: function() {
            var folder = hfFolder ? hfFolder.value : '';
            __doPostBack('gotoFolder', folder);
        },
        error: onError
    });
}
