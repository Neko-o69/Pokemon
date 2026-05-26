import java.util.Scanner;

public class Combat {

    public static void lancerCombat(Joueur j1, Joueur j2) {
        Scanner sc = new Scanner(System.in);

        while (!j1.aPerdu() && !j2.aPerdu()) {

            PokemonInstance p1 = j1.getPokemonActuel();
            PokemonInstance p2 = j2.getPokemonActuel();

            System.out.println("\n" + j1.getNom() + " : " + p1.getNom() + " (" + p1.getPv() + " PV)");
            System.out.println(j2.getNom() + " : " + p2.getNom() + " (" + p2.getPv() + " PV)");

            System.out.println("Choisis une attaque (1 = attaque simple)");
            sc.nextInt();

            attaquer(p1, p2);

            if (p2.estKO()) {
                System.out.println(p2.getNom() + " est KO !");
                j2.suivant();
                continue;
            }

            attaquer(p2, p1);

            if (p1.estKO()) {
                System.out.println(p1.getNom() + " est KO !");
                j1.suivant();
            }
        }

        if (j1.aPerdu()) {
            System.out.println(j2.getNom() + " gagne !");
        } else {
            System.out.println(j1.getNom() + " gagne !");
        }
    }

    public static void attaquer(PokemonInstance attaquant, PokemonInstance defenseur) {
        int degats = 10;

        defenseur.subirDegats(degats);

        System.out.println(attaquant.getNom() + " attaque !");
        System.out.println(defenseur.getNom() + " perd " + degats + " PV");
    }
}















using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.IO;
using System.Net.NetworkInformation;
using System.Drawing;
using Microsoft.Win32;
using System.Runtime.InteropServices;
using System.Text;
using Microsoft.Web.Infrastructure;
using System.Configuration;
using System.Web.UI;
using System.Web.UI.WebControls;


/// <summary>
/// Description résumée de NKTools
/// </summary>
public class NKTools
{
    public const short PROCESS_ACTION_PREPARE_DATA = 206;
    public const short PROCESS_ACTION_DEFLAG_DATA = 207;
    public const short PROCESS_PRINT_TO_EXCEL = 208;
    public const short PROCESS_IMPORT_DATA = 209;
    public const short PROCESS_ACTION_START_PROCESS = 210;

    public const String NLISKey = "SOFTWARE\\NIDEK S.A\\OPHELIE\\5.0";

    //[DllImport("C:\\OPHELIE\\NST.dll", EntryPoint = "StackEvent", CallingConvention = CallingConvention.Cdecl)]
    //public static extern short StackEvent(Byte[] lpDeviceName, Byte[] lpCode, Byte[] lpComment, Byte[] lpUserCode, int nStatus);

    [DllImport("NST.dll", EntryPoint = "PutTraceA", CallingConvention = CallingConvention.Cdecl)]
    public static extern short PutTrace(Byte[] lpApp, Byte[] lpFunction, Byte[] lpMessage);

    public NKTools()
    {
        //
        // TODO: Add constructor logic here
        //
    }

    public static object RegGetValue(string strSubKey, string strItem)
    {
        RegistryKey valKey;
        RegistryKey lmKey = Registry.LocalMachine;  //Registry.CurrentUser; // Registry.LocalMachine;
        String strKey = NLISKey + strSubKey;
        valKey = lmKey.OpenSubKey(strKey, false); //|-- Overture en Lecture
        if (valKey == null) return null;

        return valKey.GetValue(strItem);
    }

    public static short RegGetShort(string strSubKey, string strItem)
    {
        short nValue = 0;
        RegistryKey valKey;
        RegistryKey lmKey = Registry.LocalMachine;  //Registry.CurrentUser; // Registry.LocalMachine;
        String strKey = NLISKey + strSubKey;
        valKey = lmKey.OpenSubKey(strKey, false); //|-- Overture en Lecture
        if (valKey == null) return 0;

        try
        {
            nValue = Convert.ToInt16(valKey.GetValue(strItem));
        }
        catch (Exception ex)
        {
            return 0;
        }

        return nValue;
    }

    public static String MakeCode(String strPrefix)
    {
        DateTime dt = DateTime.Now;

        String strCode = strPrefix + dt.Year.ToString("D4") + dt.Month.ToString("D2") + dt.Day.ToString("D2")
            + dt.Hour.ToString("D2") + dt.Minute.ToString("D2") + dt.Second.ToString("D2");

        return strCode;
    }

    public static String MakeExamCode()
    {
        //DateTime dt = DateTime.Now;
        //String strCode = "EXA" + dt.Year.ToString("D4") + dt.Month.ToString("D2") + dt.Day.ToString("D2")
        //    + dt.Hour.ToString("D2") + dt.Minute.ToString("D2") + dt.Second.ToString("D2");
        //return strCode;
        return MakeCode("EXA");
    }

    public static String MakeExamImageCode()
    {
        //DateTime dt = DateTime.Now;
        //String strCode = "EXI" + dt.Year.ToString("D4") + dt.Month.ToString("D2") + dt.Day.ToString("D2")
        //    + dt.Hour.ToString("D2") + dt.Minute.ToString("D2") + dt.Second.ToString("D2");
        //return strCode;
        return MakeCode("EXI");
    }


    public static String MakePatientCode()
    {
        //DateTime dt = DateTime.Now;
        //String strCode = "CUS" + dt.Year.ToString("D4") + dt.Month.ToString("D2") + dt.Day.ToString("D2")
        //    + dt.Hour.ToString("D2") + dt.Minute.ToString("D2") + dt.Second.ToString("D2");
        //return strCode;
        return MakeCode("CUS");
    }

    public static bool IsDate(String strDate)
    {
        bool bIsDate = false;
        try
        {
            DateTime myDateTime = DateTime.Parse(strDate);
            bIsDate = true;
        }
        catch { }
        return (bIsDate);
    }

    public static bool IsFolderExist(String strFolder)
    {
        DirectoryInfo di = new DirectoryInfo(strFolder);
        return di.Exists;
    }

    public static bool IsFileExist(String strFile)
    {
        if (String.IsNullOrEmpty(strFile)) return false;
        FileInfo fi = new FileInfo(strFile);
        return fi.Exists;
    }

    public static void CreateExamDir(String strRoot, String strPatientCode, String strExamCode)
    {
        String strFolder;
        DirectoryInfo di;

        strFolder = strRoot + "\\" + strPatientCode + "\\" + strExamCode;
        if (IsFolderExist(strFolder) == false)
        {
            di = Directory.CreateDirectory(strFolder);
            if (di.Exists == false) return;
        }
    }

    public static void CreateDocumentDir(String strRoot, String strPatientCode)
    {
        String strFolder;
        DirectoryInfo di;

        strFolder = strRoot + "\\" + strPatientCode + "\\Document";
        if (IsFolderExist(strFolder) == false)
        {
            di = Directory.CreateDirectory(strFolder);
            if (di.Exists == false) return;
        }
    }

    public static void CreateDir(String strRoot)
    {
        String strFolder;
        DirectoryInfo di;

        strFolder = strRoot;
        if (IsFolderExist(strFolder) == false)
        {
            di = Directory.CreateDirectory(strFolder);
            if (di.Exists == false) return;
        }
    }

    public static void CreateOrderDir(String strRoot, String strPatientCode)
    {
        String strFolder;
        DirectoryInfo di;

        strFolder = strRoot + "\\" + strPatientCode + "\\Order";
        if (IsFolderExist(strFolder) == false)
        {
            di = Directory.CreateDirectory(strFolder);
            if (di.Exists == false) return;
        }
    }

    public static String GetMacAddress()
    {
        NetworkInterface[] nwis = NetworkInterface.GetAllNetworkInterfaces();
        String strMac = "";

        foreach (NetworkInterface ni in nwis)
        {
            IPInterfaceProperties IPProperties = ni.GetIPProperties();
            strMac = strMac + ni.GetPhysicalAddress().ToString() + " - ";
        }

        return strMac;
    }

    public static bool ThumbnailCallback()
    {
        return true;
    }

    public static String MakeThumbnail(String strDir, String strFileName)
    {
        int thumbWidth = 128;
        String strImage = strDir + "\\" + strFileName;
        System.Drawing.Image image = System.Drawing.Image.FromFile(strImage);
        int srcWidth = image.Width;
        int srcHeight = image.Height;
        int nDelta = 0;

        if (image.Width != image.Height)
        {
            if (image.Width > image.Height)
            {
                nDelta = (image.Width - image.Height) / 2;
                srcWidth = image.Height;
            }
        }


        int thumbHeight = Convert.ToInt32((Convert.ToDouble(srcHeight) / Convert.ToDouble(srcWidth)) * Convert.ToDouble(thumbWidth));

        Bitmap bmp = new Bitmap(thumbWidth, thumbHeight);

        System.Drawing.Graphics gr = System.Drawing.Graphics.FromImage(bmp);
        gr.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.HighQuality;
        gr.CompositingQuality = System.Drawing.Drawing2D.CompositingQuality.HighQuality;
        gr.InterpolationMode = System.Drawing.Drawing2D.InterpolationMode.High;

        System.Drawing.Rectangle rectDestination = new System.Drawing.Rectangle(0, 0, thumbWidth, thumbHeight);
        gr.DrawImage(image, rectDestination, nDelta, 0, srcWidth, srcHeight, GraphicsUnit.Pixel);

        String strThumb = strImage + ".jpeg";
        bmp.Save(strThumb);

        bmp.Dispose();
        image.Dispose();

        return strThumb;

    }

    public static String ByteToString(byte[] lpData, int nOffset, int nLenght)
    {
        String strResult = "";
        Byte[] lpText = new Byte[nLenght + 10];

        GCHandle pinnedArray = GCHandle.Alloc(lpText, GCHandleType.Pinned);
        IntPtr lpPointer = pinnedArray.AddrOfPinnedObject();
        Marshal.Copy(lpData, nOffset, lpPointer, nLenght);
        lpText[nLenght] = 0;
        strResult = Marshal.PtrToStringAnsi(lpPointer);
        pinnedArray.Free();

        return strResult;
    }

    public static byte[] StringToByteArray(String strText)
    {
        System.Text.UTF8Encoding encoding = new System.Text.UTF8Encoding();
        return encoding.GetBytes(strText);
    }

    public static void InsertEvent(String strCode, String strComment, String strUserCode, int nStatus)
    {
        try
        {
            String strComputer = HttpContext.Current.Request.ServerVariables["REMOTE_HOST"].ToString();
            strComputer = System.Net.Dns.GetHostEntry(HttpContext.Current.Request.ServerVariables["remote_addr"]).HostName;

            Byte[] lpDeviceName = NKTools.StringToByteArray(strComputer);
            Byte[] lpCode = NKTools.StringToByteArray(strCode);
            Byte[] lpComment = NKTools.StringToByteArray(strComment);
            Byte[] lpUserCode = NKTools.StringToByteArray(strUserCode);
            //StackEvent(lpDeviceName, lpCode, lpComment, lpUserCode, nStatus);
        }
        catch (Exception ex)
        {
            Trace("NKTools::InsertEvent", ex.Message);
        }
    }

    public static bool InsertHistory(String strUserCode, String strCusCode, String strNumber, String strType, String strDescription)
    {
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        String strCode = NKTools.MakeCode("CH");
        String strSQL = "insert into CUSTOMER_HISTORY(CHI_CODE, CHI_CUS_CODE, CHI_EMP_CODE, CHI_DATE, CHI_NUMBER, CHI_TYPE, CHI_DESCRIPTION, CHI_STATUS ) values (" +
        "'" + strCode + "', '" + strCusCode + "', '" + strUserCode + "', " + DBHelper.GetCurrentDate() + ", '" + strNumber +
        "', '" + strType + "', '" + strDescription + "', 1 )";
        return DBHelper.SQLExecute(strSQL, strConnection);
    }

    public static bool InsertHistory(String strCusCode, String strNumber, String strType, String strDescription)
    {
        if (HttpContext.Current.Session["User"] == null) return false;
        NKUser nkUser = (NKUser)HttpContext.Current.Session["User"];
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        String strCode = NKTools.MakeCode("CH");
        String strSQL = "insert into CUSTOMER_HISTORY(CHI_CODE, CHI_CUS_CODE, CHI_EMP_CODE, CHI_DATE, CHI_NUMBER, CHI_TYPE, CHI_DESCRIPTION, CHI_STATUS ) values (" +
        "'" + strCode + "', '" + strCusCode + "', '" + nkUser.m_strCode + "', " + DBHelper.GetCurrentDate() + ", '" + strNumber +
        "', '" + strType + "', '" + strDescription + "', 1 )";
        return DBHelper.SQLExecute(strSQL, strConnection);
    }


    public static void Trace(String strFunction, String strMessage)
    {
        String strApp = "OPHELIE";

        Byte[] lpApp = NKTools.StringToByteArray(strApp);
        Byte[] lpFunction = NKTools.StringToByteArray(strFunction);
        Byte[] lpMessage = NKTools.StringToByteArray(strMessage);
        //PutTrace(lpApp, lpFunction, lpMessage);
    }

    public static String ToAscii(String strDirty)
    {
            //ASCIIEncoding asciiEncoding = new ASCIIEncoding();
            //byte[] bytes = Encoding.GetEncoding("ISO-8859-1").GetBytes(strDirty);
            ////byte[] asciiArray = Encoding.Convert(Encoding.UTF8, Encoding.ASCII, bytes);
            ////String strClean = asciiEncoding.GetString(asciiArray);
            //String strClean = Encoding.ASCII.GetString(bytes);
            //return strClean;

            //byte[] bytes = Encoding.GetEncoding("iso-8859-8").GetBytes(strDirty);
        Encoding en = Encoding.GetEncoding("iso-8859-1");
        return en.GetString(Encoding.Convert(Encoding.UTF8, en, Encoding.UTF8.GetBytes(strDirty)));

    }

    public static short StringToShort(String strNumber)
    {
        short nValue = 0;
        if (short.TryParse(strNumber, out nValue)) return nValue;
        return (short)0;
    }


    public static int StringToInt(String strNumber)
    {
        int nValue = 0;
        if (int.TryParse(strNumber, out nValue)) return nValue;
        return (int)0;
    }

    public static double StringToDouble(String strNumber)
    {
        double dValue = 0;
        String strValue = "";
        char n;
        foreach (char nChar in strNumber)
        {
            n = nChar;
            if (n == '.') n = ','; //|-- on fcontionne en mode french
            if ((n >= 48 && n <= 57) || n == ',' || n == '+' || n == '-')
                strValue += n;
        }

        if (strValue.Length <= 0) return 0.0;

        if (double.TryParse(strValue, out dValue)) return dValue;

        return 0.0;
    }

    public static String MakeHTMLString(String strValue)
    {
        String strResult = strValue.Replace("à", "&agrave;");
        strResult = strResult.Replace("é", "&eacute;");
        strResult = strResult.Replace("è", "&egrave;");
        strResult = strResult.Replace("ê", "&ecirc;");
        return strResult;
    }

    public static void WriteCommandToFile(HttpServerUtility Server, short nAction, short nMessageSize, String strMessage)
    {
        String strCdeFile = Server.MapPath("Temp\\Exchange\\Command.data");
        BinaryWriter bw;
        //|-- Create the file
        try
        {
            bw = new BinaryWriter(new FileStream(strCdeFile, FileMode.Create));
        }
        catch (Exception ex)
        {
            //Console.WriteLine(e.Message + "\n Cannot create file.");
            return;
        }

        //|-- Writing command to the file
        try
        {
            bw.Write(nAction);
            bw.Write(nMessageSize);
            if (nMessageSize > 0)
            {
                byte[] lpData = StringToByteArray(strMessage);
                bw.Write(lpData);
            }
        }
        catch (Exception ex)
        {
            //Console.WriteLine(e.Message + "\n Cannot write to file.");
            return;
        }
        bw.Close();
    }

    public static String StringToAscii(String strText)
    {
        int i;
        String strResult = "";
        byte[] StringAscII = System.Text.Encoding.ASCII.GetBytes(strText);
        for (i = 0; i < StringAscII.Length; i++)
        {
            strResult += StringAscII[i].ToString("D3");
        }
        //|-- Inverse seulement les 0 & 1
        StringBuilder strBuilder = new StringBuilder(strResult);
        for (i = 0; i < strBuilder.Length; i++)
        {
            switch (strBuilder[i])
            {
                case '0':
                    strBuilder[i] = '1';
                    break;

                case '1':
                    strBuilder[i] = '0';
                    break;
            }
        }
        strResult = strBuilder.ToString();
        return strResult;
    }

    public static String AsciiToString(String strAscii)
    {
        if (String.IsNullOrEmpty(strAscii)) return "";
        //|-- Inverse seulement les 0 & 1
        int i;
        StringBuilder strBuilder = new StringBuilder(strAscii);
        for (i = 0; i < strBuilder.Length; i++)
        {
            switch (strBuilder[i])
            {
                case '0':
                    strBuilder[i] = '1';
                    break;

                case '1':
                    strBuilder[i] = '0';
                    break;
            }
        }

        String strResult = strBuilder.ToString(), strValue = "";
        Byte[] lpText = new Byte[strResult.Length];

        int nIndex = 0, nOffset = 0;
        do
        {
            strValue = strResult.Substring(nIndex, 3);
            nIndex += 3;
            lpText[nOffset++] = Convert.ToByte(strValue);
        } while (nIndex < strResult.Length);

        lpText[nOffset] = 0;

        strResult = ByteToString(lpText, 0, nOffset);

        return strResult;
    }


    public static String FillZeroBefore(int number, int numberOfcharacter)
    {
        string strValue = "";
        for (int i = 0; i < numberOfcharacter - number.ToString().Length; i++)
        {
            strValue += "0";
        }
        strValue += number;
        return strValue;
    }

    public static String FillZeroAfter(int number, int numberOfcharacter)
    {
        string strValue = number.ToString();
        for (int i = 0; i < numberOfcharacter - number.ToString().Length; i++)
        {
            strValue += "0";
        }
        return strValue;
    }

    public static bool CheckNet()
    {
        string url = "http://www.google.fr";
        try
        {
            System.Net.WebRequest myRequest = System.Net.WebRequest.Create(url);
            System.Net.WebResponse myResponse = myRequest.GetResponse();
        }
        catch (System.Net.WebException)
        {
            return false;
        }
        return true;
    }

    public static string Replace(string strNumber)
    {
        strNumber = strNumber.Replace('/', '-');
        strNumber = strNumber.Replace('\\', '-');
        strNumber = strNumber.Replace(':', '-');
        strNumber = strNumber.Replace('?', '-');
        strNumber = strNumber.Replace('\"', '-');
        strNumber = strNumber.Replace('<', '-');
        strNumber = strNumber.Replace('>', '-');
        strNumber = strNumber.Replace('|', '-');
        strNumber = strNumber.Replace('+', '-');
        return strNumber;
    }
}
