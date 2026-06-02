var selectedFiles;

var box = document.getElementById("dropZone");

box.addEventListener("dragover", function (e) {
    e.preventDefault();
});

box.addEventListener("drop", function (e) {
    e.preventDefault();

    selectedFiles = e.dataTransfer.files;

    box.innerHTML = selectedFiles.length + " fichier(s) sélectionné(s)";
});

document.getElementById("upload").onclick = function () {

    if (!selectedFiles || selectedFiles.length == 0)
        return;

    var data = new FormData();

    for (var i = 0; i < selectedFiles.length; i++) {
        data.append("file" + i, selectedFiles[i]);
    }

    $.ajax({
        type: "POST",
        url: "FileHandler.aspx",
        data: data,
        contentType: false,
        processData: false,
        success: function () {
            alert("Upload terminé");
            location.reload();
        }
    });

  
};public partial class FileHandler : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        if (Request.Files.Count == 0)
            return;

        string connection =
            ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;

        foreach (string key in Request.Files)
        {
            HttpPostedFile file = Request.Files[key];

            string fullPath =
                "Quality/" + file.FileName.Replace("'", "&quote");

            string physicalPath =
                Server.MapPath("~/Images/") + fullPath;

            file.SaveAs(physicalPath);

            string sql =
                "INSERT INTO BASE_DOCUMENTAIRES " +
                "(BD_FULL_PATH,BD_NAME,BD_FOLDER_PARENT,BD_TYPE) VALUES (" +
                "'" + fullPath + "'," +
                "'" + Path.GetFileNameWithoutExtension(file.FileName) + "'," +
                "'Quality'," +
                "1)";

            DBHelper.SQLExecute(sql, connection);
        }

        Response.Write("OK");
        Response.End();
    }
}
