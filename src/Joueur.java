if (Request.Files.Count > 0)
{
    HttpPostedFile file = Request.Files[0];

    String strParentFolder = "Quality";

    String strFullPath =
        strParentFolder + "/" + file.FileName.Replace("'", "&quote");

    String strFileName =
        Server.MapPath("Images/") + strFullPath;

    file.SaveAs(strFileName);

    Response.Write("OK");
    Response.End();
}
