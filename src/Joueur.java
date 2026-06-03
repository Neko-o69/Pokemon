// drag and drop
var zone = document.getElementById('dropZone');

if (zone) {
    zone.ondragover = function(e) {
        e.preventDefault();
        zone.style.background = '#cce';
    }

    zone.ondragleave = function() {
        zone.style.background = '#f9f9f9';
    }

    zone.ondrop = function(e) {
        e.preventDefault();
        zone.style.background = '#f9f9f9';

        var fichier = e.dataTransfer.files[0];
        if (!fichier) return;

        var input = document.getElementById('mainContent_DocFileUpload');
        var dt = new DataTransfer();
        dt.items.add(fichier);
        input.files = dt.files;

        document.getElementById('mainContent_tbFileName').value = fichier.name;

        showUploadDlg();
    }
}
