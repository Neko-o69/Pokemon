[WebMethod(EnableSession = true)]
public static string GetFileUrl(string strFullPath)
{
    string strRoot = System.Web.Hosting.HostingEnvironment.MapPath("~/Images/");
    string strRelative = strFullPath.Replace(strRoot, "").Replace("\\", "/");
    string strBase = HttpContext.Current.Request.Url.GetLeftPart(UriPartial.Authority)
                   + HttpContext.Current.Request.ApplicationPath.TrimEnd('/');
    return strBase + "/Images/" + strRelative;
}

case 1: // Word
case 2: // Excel
case 5: // PowerPoint
case 8: // XML

    divImageViewer.style.display = 'none';
    iframe.style.display = 'block';

    if (obj != null) {
        objLastSelected = obj;
        obj.className = "linkButtonQualityItemSelected";
    }

    hfSelected.value = strFullPath;
    showInfoDlg();

    $.ajax({
        type: 'POST',
        url: 'QualityFolder.aspx/GetFileUrl',
        data: JSON.stringify({ strFullPath: strFullPath }),
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: function (data) {
            var fileUrl = data.d;
            // Microsoft Office Online Viewer (fonctionne en intranet si le fichier est accessible)
            iframe.src = "https://view.officeapps.live.com/op/embed.aspx?src=" 
                       + encodeURIComponent(fileUrl);
            hideInfoDlg();
        },
        error: function () {
            hideInfoDlg();
            alert("Impossible d'ouvrir le fichier.");
        }
    });

    break;
