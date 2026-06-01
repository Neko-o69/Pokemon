Dropzone.js is a popular JavaScript library that provides an easy way to upload files in an ASP.NET MVC application. It uses HTML5’s drag and drop API and Ajax to improve the user experience of file uploading. To use Dropzone.js in your ASP.NET MVC application, follow these steps:

 

Step 1: Download Dropzone.js

 

 Alternatively, you can use a CDN to load the library. Add the following code to your project’s layout page, preferably just before the closing “body” tag: “`

<script src=”https://cdnjs.cloudflare.com/ajax/libs/dropzone/5.7.2/min/dropzone.min.js”></script> <link rel=”stylesheet” href=”https://cdnjs.cloudflare.com/ajax/libs/dropzone/5.7.2/min/dropzone.min.css” />

Step 2: Setup a Form Next, create a form in your view that will contain the Dropzone.js instance. You can use regular HTML form elements or use the ASP.NET MVC helper methods to create the form. For example:

@using (Html.BeginForm(“Upload”, “Home”, FormMethod.Post, new { enctype = “multipart/form-data”, @class = “dropzone”, id = “file-upload-form” })) { <div class=”fallback”> <input type=”file” name=”file” /> </div> <button type=”submit” class=”btn btn-primary”>Upload</button> }

Note that we are using the “dropzone” CSS class to define the form, which is important for Dropzone.js to work properly.

Step 3: Configure Dropzone In the same view, add the following JavaScript code to configure Dropzone.js:

<script type=”text/javascript”> $(document).ready(function () { Dropzone.autoDiscover = false; $(“#file-upload-form”).dropzone({ // Set any desired options for dropzone // For example, you can set upload URL and max file size url: ‘@Url.Action(“Upload”, “Home”)’, maxFilesize: 3, maxFiles: 10 }); });

</script> “` In the above code, we have disabled automatic discovering of Dropzone instances, as we are manually initializing it on our form. Then we are providing a custom upload URL and setting the max file size and max files allowed.

Step 4:

MVC controller, create an action method to handle the file upload. For this example, we will create a method called “Upload” in the “HomeController”: “` [HttpPost] public ActionResult Upload(HttpPostedFileBase file) { if (file != null && file.ContentLength > 0) { var fileName = Path.GetFileName(file.FileName); var path = Path.Combine(Server.MapPath(“~/App_Data/uploads”), fileName); file.SaveAs(path); } return RedirectToAction(“Index”); }

Step 5: Test the Upload Run your ASP.NET MVC application and browse to the view containing the form.
