    function onItemClicked(obj, nType, strFullPath) {
        if (objLastSelected != null) {
            objLastSelected.className = "linkButtonQualityItem";

        }

        var hfSelected = document.getElementById('mainContent_hfSelectedFile');
        hfSelected.value = "";
        var hfSelectedType = document.getElementById('mainContent_hfSelectedType');
        hfSelectedType.value = nType;

        var iframe = document.getElementById('ifDoc');
        if (iframe == null) return;
        var divImgViewer = document.getElementById('divImageViewer');
        if (divImageViewer == null) return;

        //var strDisplay = divImageViewer.style.display;

        switch (nType) {
            //|------------------------------------------------------
            // Word ou Excel, ou PowerPoint
            //|------------------------------------------------------
            case 1: // doc
            case 2: // xls
            case 5: // ppt
            case 8: // xml

                divImageViewer.style.display = 'none';
                iframe.style.display = 'block';

                showInfoDlg();
                // Pour google
                iframe.src = "https://docs.google.com/gview?url=http://nidek-w2k8-mob.nidek.fr:5004/Images/" + encodeURI(strFullPath) + "&embedded=true";
                //iframe.src = "Images/" + encodeURI(strFullPath) ;

                window.open("/Images/") + encodeURI(strFullPath),'_blank);'



                // Pour Microsoft                    
                //iframe.src = "http://view.officeapps.live.com/op/view.aspx?src=" + window.location.host + "/Images/" + strFullPath;
                //iframe.src = "http://view.officeapps.live.com/op/view.aspx?src=http://nidek-w2k8-mob.nidek.fr:5004/Images/" + encodeURI(strFullPath);


                if (obj != null) {
                    objLastSelected = obj;
                    obj.className = "linkButtonQualityItemSelected";
                }
                hfSelected.value = strFullPath;
                break;

            //|------------------------------------------------------
            // PDF
            //|------------------------------------------------------
            case 3:

                divImageViewer.style.display = 'none';
                iframe.style.display = 'block';

                showInfoDlg();
                // By self
                iframe.src = "/Images/" + strFullPath;
                // Par Google
                //iframe.src = "http://docs.google.com/gview?url=" +
                //    window.location.host + "/Images/" + strFullPath + "&embedded=true";
                window.open("/Images/" + strFullPath, '_blank');
                // Par Microsoft
                //iframe.src = "http://view.officeapps.live.com/op/view.aspx?src=" +
                //        window.location.host + "/Images/" + strFullPath;

                if (obj != null) {
                    objLastSelected = obj;
                    obj.className = "linkButtonQualityItemSelected";
                }
                hfSelected.value = strFullPath;
                break;

            //|------------------------------------------------------
            // Image
            //|------------------------------------------------------
            case 4:

                divImageViewer.style.display = 'block';
                iframe.style.display = 'none';

                hideInfoDlg();

                //|----------------------------------------------------
                // Recharge l'image
                //|----------------------------------------------------
                if (osdViewer != null) {
                    //viewer.tileSources.url = "/images/" + strFullPath;
                    osdViewer.destroy();
                    osdViewer = OpenSeadragon({
                        id: "divImageViewer",
                        //prefixUrl: "/images/",
                        //tileSources: "/path/to/my/image.dzi"
                        tileSources: {
                            type: 'image',
                            url: "/images/" + strFullPath
                        }
                    });
                }

                if (obj != null) {
                    objLastSelected = obj;
                    obj.className = "linkButtonQualityItemSelected";
                }
                hfSelected.value = strFullPath;

                break;

            //|------------------------------------------------------
            // Zip, Mpp
            //|------------------------------------------------------
            case 6:  //zip
            case 7: // mpp
            case 9://bin
            case 10://dwg
                divImageViewer.style.display = 'none';
                iframe.style.display = 'block';


                // By self
                iframe.style.display = 'none';
                hideInfoDlg();
                osdViewer.destroy();
                //osdViewer.style.display = 'none';

                if (obj != null) {
                    objLastSelected = obj;
                    obj.className = "linkButtonQualityItemSelected";
                }
                hfSelected.value = strFullPath;
                break;

            default:    // C'est le folder
                __doPostBack('gotoFolder', strFullPath);
                hfSelected.value = "";
                break;
        }
    }

</script>
