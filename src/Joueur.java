Traditionnellement, on utilise le champ fichier HTML pour sélectionner les fichiers à télécharger sur le serveur. ASP.NET formulaires web encapsulent le champ fichier dans le contrôle du serveur FileUpload et ASP.NET applications MVC peuvent utiliser un élément <input> avec l’attribut type défini sur file. Une autre alternative proposée par HTML5 consiste à glisser un ou plusieurs fichiers depuis Windows Explorer ou Desktop et à les déposer sur un élément HTML prédéfini d’une page web. Vous pouvez alors accéder aux fichiers déposés en utilisant l’objet dataTransfer disponible pour glisser-déposer les événements. Discuter du glisser-déposer HTML5 depuis le début dépasse le cadre de cet article. Si vous ne connaissez pas le HTML5, glisser-déposez, lisez d’abord cet article.

Pour comprendre comment sélectionner des fichiers grâce aux fonctions de glisser-déposer de HTML5, développons une nouvelle application ASP.NET formulaires web. Le balisage HTML du formulaire web par défaut est présenté ci-dessous :

<form id="form1" runat="server">
<center>
  <div id="box">Drag & Drop files from your machine on this box.</div>
  <br />
  <input id="upload" type="button" value="Upload Selected Files" />
</center>
</form>
Comme vous pouvez le voir, la < forme > se compose d’un élément < div> et d’un bouton. L’élément <div> est destiné à déposer les fichiers traînés depuis la machine locale. Se contenter de déposer les fichiers ne les téléversera pas sur le serveur. Cliquer sur le bouton lance l’opération de téléchargement de fichiers.

Pour gérer l’opération de dépôt de fichiers, il faut connecter certains gestionnaires d’événements à l’élément boîte <div>. Le code jQuery suivant montre comment cela peut être fait :

var selectedFiles;

$(document).ready(function () {
  var box;
  box = document.getElementById("box");
  box.addEventListener("dragenter", OnDragEnter, false);
  box.addEventListener("dragover", OnDragOver, false);
  box.addEventListener("drop", OnDrop, false);
...
}
Le code déclare une variable globale nommée selectedFiles pour stocker une liste de fichiers sélectionnés. La fonction ready() connecte trois fonctions gestionnaire d’événements aux événements respectifs de l’élément boîte <div>, à savoir dragenter, dragover and drop en utilisant la méthode addEventListener(). Le premier paramètre de la méthode addEventListener() est le nom de l’événement et le second paramètre est la fonction gestionnaire d’événements. Les fonctions du gestionnaire d’événements sont indiquées ci-dessous :

function OnDragEnter(e) {
  e.stopPropagation();
  e.preventDefault();
}

function OnDragOver(e) {
  e.stopPropagation();
  e.preventDefault();
}

function OnDrop(e) {
  e.stopPropagation();
  e.preventDefault();
  selectedFiles = e.dataTransfer.files;
  $("#box").text(selectedFiles.length + " file(s) selected for uploading!");
}
Les fonctions de gestionnaire d’événements OnDragEnter() et OnDragOver() sont simples et empêchent simplement le déploiement des événements respectifs. La fonction OnDrop() est importante car elle gère l’événement de chute. La liste des fichiers glissés et déposés sur l’élément <div> est obtenue en utilisant la propriété fichiers de l’objet dataTransfer. L’objet fichiers est de type FileList et chaque élément de la collection FileList est de type File. Ces deux objets sont disponibles dans le cadre de l’API de fichiers HTML5. La fonction OnDrop() stocke les fichiers sélectionnés dans la variable globale – selectedFiles et affiche un message dans la <div> utilisant la méthode text() qui indique le nombre de fichiers sélectionnés. La figure suivante montre à quoi ressemble le formulaire web par défaut après avoir glissé et déposé des fichiers sur l’élément <div>.

Formulaire web par défaut après avoir glissé et déposé des fichiers vers l’élément <div>
 

Formulaire web par défaut après avoir glissé et déposé des fichiers vers l’élément <div>

Envoi de fichiers au serveur en utilisant jQuery
Pour envoyer les fichiers sélectionnés du client vers le serveur, vous pouvez utiliser différentes techniques, mais dans cet exemple, vous utiliserez la méthode jQuery $.ajax() pour télécharger les fichiers. Le code suivant montre comment la méthode $.ajax() peut être utilisée à cette fin.

$("#upload").click(function () {
  var data = new FormData();
  for (var i = 0; i < selectedFiles.length; i++) {
    data.append(selectedFiles[i].name, selectedFiles[i]);
  }
  $.ajax({
    type: "POST",
    url: "FileHandler.ashx",
    contentType: false,
    processData: false,
    data: data,
    success: function (result) {
      alert(result);
    },
    error: function () {
      alert("There was error uploading files!");
    }
  });
});
Le code montré ci-dessus crée d’abord un objet FormData. L’objet FormData encapsule les données de Form que vous souhaitez envoyer au serveur. Tous les fichiers sélectionnés sont ajoutés à l’objet FormData en utilisant sa méthode append(). Le premier paramètre de la méthode append() est le nom du fichier ajouté et le second paramètre est l’objet fichier lui-même. Une fois l’objet FormData prêt, vous effectuez une requête POST à un gestionnaire de ASP.NET générique (FileHandler.ashx) en utilisant la méthode jQuery $.ajax(). Vous créerez le gestionnaire générique dans la section suivante.

Le type de demande est POST. L’URL est FileHandler.ashx. Remarquez que l’option processData est réglée sur false. Par défaut, lorsque vous utilisez la méthode $.ajax(), les données sont envoyées au format encodé par URL. Pour éviter ce comportement, processData est réglé sur false. L’option de données est définie sur l’objet FormData créé précédemment. La fonction de succès affiche simplement le message retourné par le gestionnaire générique. La fonction gestionnaire d’erreur affiche un message d’erreur au cas où il y aurait une erreur lors de l’appel de FileHandler.ashx.

Réception des fichiers téléchargés sur le serveur
Le gestionnaire générique ASP.NET – FileHandler.ashx – reçoit les fichiers envoyés par la méthode $.ajax(). Le gestionnaire générique les sauvegarde aussi dans un dossier du serveur. Le code suivant montre comment le gestionnaire accomplit cette tâche :

public void ProcessRequest(HttpContext context)
{
    if (context.Request.Files.Count > 0)
    {
        HttpFileCollection files = context.Request.Files;
        foreach (string key in files)
        {
            HttpPostedFile file = files[key];
            string fileName = file.FileName;
            fileName = context.Server.MapPath("~/uploads/" + fileName);
            file.SaveAs(fileName);
        }
    }
    context.Response.ContentType = "text/plain";
    context.Response.Write("File(s) uploaded successfully!");
}
La méthode ProcessRequest() du FileHandler.ashx est appelée lorsque les fichiers sont envoyés au serveur en utilisant la méthode $.ajax(). Les fichiers téléchargés peuvent être consultés via la collection Files de l’objet Request. Chaque élément de la collection Fichiers est de type HttpPostedFile. Une boucle foreach itère tous les fichiers de la collection Files et sauvegarde le fichier individuel en utilisant la méthode SaveAs() de la classe HttpPostedFile. Une fois tous les fichiers sauvegardés, un message de réussite est envoyé au client.

Notez que par défaut ASP.NET règle la longueur de la requête à 4096 octets. Si vous souhaitez télécharger de gros fichiers, vous pouvez ajuster la longueur de la requête en utilisant le fichier web.config comme indiqué ci-dessous :

 <httpRuntime
   maxRequestLength="20000"
   requestValidationMode="4.5"
   targetFramework="4.5"
   encoderType="..." />
Comme vous pouvez le voir, l’attribut maxRequestLength de la section <httpRuntime> est réglé à 20 000 octets. Vous devez ajuster cette valeur selon vos besoins.

C’est ça ! Vous pouvez maintenant lancer le formulaire web, glisser-déposer des fichiers sur l’élément <div> et cliquer sur le bouton « Télécharger des fichiers sélectionnés » pour les télécharger sur le serveur.
