using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data.SqlClient;
using System.Configuration;
using System.Data;
using System.Data.Common;
using System.Data.Odbc;

/// <summary>
/// Description résumée de DBHelper
/// </summary>
public class DBHelper
{
	public DBHelper()
	{
		//
		// TODO: Add constructor logic here
		//
	}

    public static DataTable GetTable(String strSQL, int nMaxRecord = 50)
    {
        DataTable m_DataTable = new DataTable();

        //|-- D'abord récupérer le provider
        DbProviderFactory dbProviderFactory = DbProviderFactories.GetFactory("System.Data.Odbc");
        //|-- Chaine de connection
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        //|-- Object Database connection
        DbConnection dbConnect = dbProviderFactory.CreateConnection();


        //m_SQLConnect = new SqlConnection("Dsn=OPHELIE;uid=NIDEK;pwd=NIDEK");
        dbConnect.ConnectionString = strConnection;
        dbConnect.Open();

        //|-- Créer mainetant la Query qui va exécuter cette chaine SQL
        DbCommand dbCommand = dbConnect.CreateCommand();
        dbCommand.CommandText = strSQL;

        //|-- Crée l'adapter pour remplir le DataSet
        DbDataAdapter dbAdapter = dbProviderFactory.CreateDataAdapter();
        dbAdapter.SelectCommand = dbCommand;

        try
        {
            dbAdapter.Fill(0, nMaxRecord, m_DataTable);
        }
        catch { }
        finally
        {
            //m_SQLConnect.Close();
            dbConnect.Close();
        }

        return m_DataTable;
    }

    public static DataTable GetTable(String strSQL, String strConnection, int nMaxRecord = 50)
    {
        DataTable m_DataTable = new DataTable();

        //|-- D'abord récupérer le provider
        DbProviderFactory dbProviderFactory = DbProviderFactories.GetFactory("System.Data.Odbc");

        //|-- Object Database connection
        DbConnection dbConnect = dbProviderFactory.CreateConnection();

        dbConnect.ConnectionString = strConnection;
        dbConnect.Open();

        //|-- Créer mainetant la Query qui va exécuter cette chaine SQL
        DbCommand dbCommand = dbConnect.CreateCommand();
        dbCommand.CommandText = strSQL;

        //|-- Crée l'adapter pour remplir le DataSet
        DbDataAdapter dbAdapter = dbProviderFactory.CreateDataAdapter();
        dbAdapter.SelectCommand = dbCommand;

        try
        {
            dbAdapter.Fill(0, nMaxRecord, m_DataTable);
        }
        catch { }
        finally
        {
            //m_SQLConnect.Close();
            dbConnect.Close();
        }

        return m_DataTable;
    }

    public static DataSet Open(String strSQL, String strConnection)
    {
        DataSet m_DataTable = new DataSet();

        //|-- D'abord récupérer le provider
        DbProviderFactory dbProviderFactory = DbProviderFactories.GetFactory("System.Data.Odbc");
        //|-- Chaine de connection
        //String strConnection = ConfigurationManager.ConnectionStrings["SAWA"].ConnectionString;
        //|-- Object Database connection
        DbConnection dbConnect = dbProviderFactory.CreateConnection();


        //m_SQLConnect = new SqlConnection("Dsn=OPHELIE;uid=NIDEK;pwd=NIDEK");
        dbConnect.ConnectionString = strConnection;
        dbConnect.Open();

        //|-- Créer mainetant la Query qui va exécuter cette chaine SQL
        DbCommand dbCommand = dbConnect.CreateCommand();
        dbCommand.CommandText = strSQL;

        //|-- Crée l'adapter pour remplir le DataSet
        DbDataAdapter dbAdapter = dbProviderFactory.CreateDataAdapter();
        dbAdapter.SelectCommand = dbCommand;

        try
        {
            dbAdapter.Fill(m_DataTable);
        }
        catch(Exception ex)
        {
            NKTools.Trace("DBHelper::SQLExecute", ex.Message);
            if (strSQL.Length > 512)
            {
                int nOffset = 0, nLen = 512;
                do
                {
                    NKTools.Trace("DBHelper::SQL", strSQL.Substring(nOffset, nLen));
                    nOffset += nLen;
                    nLen = strSQL.Length - nOffset;
                } while (nLen > 0);
            }
            else
                NKTools.Trace("DBHelper::SQL", strSQL);

        }
        finally
        {
            dbConnect.Close();
        }

        return m_DataTable;
    }

    public static DataTable SQLOpen(String strSQL, String strConnection)
    {
        DataTable m_DataTable = new DataTable();

        //|-- D'abord récupérer le provider
        DbProviderFactory dbProviderFactory = DbProviderFactories.GetFactory("System.Data.Odbc");
        //|-- Chaine de connection
        //String strConnection = ConfigurationManager.ConnectionStrings["SAWA"].ConnectionString;
        //|-- Object Database connection
        DbConnection dbConnect = dbProviderFactory.CreateConnection();


        //m_SQLConnect = new SqlConnection("Dsn=OPHELIE;uid=NIDEK;pwd=NIDEK");
        dbConnect.ConnectionString = strConnection;
        dbConnect.Open();

        //|-- Créer mainetant la Query qui va exécuter cette chaine SQL
        DbCommand dbCommand = dbConnect.CreateCommand();
        dbCommand.CommandText = strSQL;

        //|-- Crée l'adapter pour remplir le DataSet
        DbDataAdapter dbAdapter = dbProviderFactory.CreateDataAdapter();
        dbAdapter.SelectCommand = dbCommand;

        try
        {
            dbAdapter.Fill(m_DataTable);
        }
        catch(Exception ex)
        {
            NKTools.Trace("DBHelper::SQLExecute", ex.Message);
            if (strSQL.Length > 512)
            {
                int nOffset = 0, nLen = 512;
                do
                {
                    NKTools.Trace("DBHelper::SQL", strSQL.Substring(nOffset, nLen));
                    nOffset += nLen;
                    nLen = strSQL.Length - nOffset;
                } while (nLen > 0);
            }
            else
                NKTools.Trace("DBHelper::SQL", strSQL);
        }
        finally
        {
            dbConnect.Close();
        }

        //DataTable m_DataTable = new DataTable();
        //OdbcConnection conn = new OdbcConnection(strConnection);
        //OdbcDataAdapter adapter = new OdbcDataAdapter();
        //adapter.SelectCommand = new OdbcCommand(strSQL, conn);
        //adapter.Fill(m_DataTable);

        return m_DataTable;
    }


    public static bool SQLExecute(String strSQL, String strConnection)
    {
        bool bRetCode = false;
        //|-- D'abord récupérer le provider
        DbProviderFactory dbProviderFactory = DbProviderFactories.GetFactory("System.Data.Odbc");
        //|-- Chaine de connection
        //String strConnection = ConfigurationManager.ConnectionStrings["SAWA"].ConnectionString;
        //|-- Object Database connection
        DbConnection dbConnect = dbProviderFactory.CreateConnection();

        try
        {
            //|-- Attribute la chaine DNS à la connection database
            dbConnect.ConnectionString = strConnection;
            //|-- Ouvre la connection de la database
            dbConnect.Open();
            //|-- Créer mainetant la Query qui va exécuter cette chaine SQL
            DbCommand dbCom = dbConnect.CreateCommand();
            dbCom.CommandText = strSQL;
            dbCom.ExecuteNonQuery();

            bRetCode = true;

            //SqlConnection sqlConnect = new SqlConnection(strConnection);
            //SqlCommand sqlCom = new SqlCommand(strSQL, sqlConnect);
            //sqlConnect.Open();
            //sqlCom.ExecuteNonQuery();
            //sqlConnect.Close();
        }
        catch (Exception ex)
        {
            NKTools.Trace("DBHelper::SQLExecute", ex.Message);
            if (strSQL.Length > 512)
            {
                int nOffset = 0, nLen = 512;
                do
                {
                    NKTools.Trace("DBHelper::SQL", strSQL.Substring(nOffset, nLen));
                    nOffset += nLen;
                    nLen = strSQL.Length - nOffset;
                } while (nLen > 0);
            }
            else
                NKTools.Trace("DBHelper::SQL", strSQL);
        }
        finally
        {
            dbConnect.Close();
        }

        return bRetCode;

    }

    public static bool SQLInsert(DataTable dt, int nIndex, String strTable, String strConnection)
    {
        bool bRetCode = false, bFirstCol = true;
        DateTime date;
        //|-- D'abord récupérer le provider
        DbProviderFactory dbProviderFactory = DbProviderFactories.GetFactory("System.Data.Odbc");
        //|-- Chaine de connection
        //|-- Object Database connection
        DbConnection dbConnect = dbProviderFactory.CreateConnection();

        String strType = "", strValue = "", strSQL = "insert into " + strTable + " values (";
        if (dt.Rows.Count < nIndex)
        {
            //|-- No more line
            return false;
        }

        //|-- Building data
        bFirstCol = true;
        foreach (DataColumn col in dt.Columns)
        {
            if (dt.Rows[nIndex][col.ColumnName] == DBNull.Value)
            {
                //strValue = col.ColumnName + " = NULL";
                strValue = "NULL";
            }
            else
            {
                strType = col.DataType.Name;
                switch (strType)
                {
                    case "DateTime":
                        //strValue = "#" + Convert.ToDateTime(dt.Rows[nIndex][col.ColumnName]).ToString("yyyy-MM-dd HH:mm:ss") + "#";
                        date = Convert.ToDateTime(dt.Rows[nIndex][col.ColumnName]);
                        strValue = DateToSQL(date, 3);
                        break;

                    case "Guid":
                        //strValue = col.ColumnName + " = NULL";
                        strValue = "NULL";
                        break;

                    case "Double":
                    case "Single":
                    case "Decimal":
                        strValue = dt.Rows[nIndex][col.ColumnName].ToString();
                        strValue = strValue.Replace(',', '.');
                        break;

                    case "Boolean":
                    case "Byte":
                    case "Int16":
                    case "Int32":
                    case "Int64":
                    case "SByte":
                    case "UInt16":
                    case "UInt32":
                    case "UInt64":
                        //strValue = col.ColumnName + " = " + dt.Rows[nIndex][col.ColumnName].ToString();
                        strValue = dt.Rows[nIndex][col.ColumnName].ToString();
                        break;

                    case "TimeSpan":
                        //strValue = col.ColumnName + " = NULL";
                        strValue = "NULL";
                        break;

                    //case "Char":
                    //|case "String":
                    default:
                        //strValue = col.ColumnName + " = '" + DBHelper.CharToSQL(dt.Rows[nIndex][col.ColumnName].ToString()) + "'";
                        strValue = "'" + DBHelper.CharToSQL(dt.Rows[nIndex][col.ColumnName].ToString()) + "'";
                        break;

                }
            }

            if (bFirstCol)
            {
                strSQL += strValue;
                bFirstCol = false;
            }
            else
                strSQL += ", " + strValue;
        }

        strSQL += ")";

        try
        {
            //|-- Attribute la chaine DNS à la connection database
            dbConnect.ConnectionString = strConnection;
            //|-- Ouvre la connection de la database
            dbConnect.Open();
            //|-- Créer mainetant la Query qui va exécuter cette chaine SQL
            DbCommand dbCom = dbConnect.CreateCommand();
            dbCom.CommandText = strSQL;
            dbCom.ExecuteNonQuery();

            bRetCode = true;

            //SqlConnection sqlConnect = new SqlConnection(strConnection);
            //SqlCommand sqlCom = new SqlCommand(strSQL, sqlConnect);
            //sqlConnect.Open();
            //sqlCom.ExecuteNonQuery();
            //sqlConnect.Close();
        }
        catch (Exception ex)
        {
            //Global.Debug.Trace("SQLInsert : " + ex.Message);
            //Global.Debug.Trace(strSQL);
            NKTools.Trace("DBHelper::SQLExecute", ex.Message);
            if (strSQL.Length > 512)
            {
                int nOffset = 0, nLen = 512;
                do
                {
                    NKTools.Trace("DBHelper::SQL", strSQL.Substring(nOffset, nLen));
                    nOffset += nLen;
                    nLen = strSQL.Length - nOffset;
                } while (nLen > 0);
            }
            else
                NKTools.Trace("DBHelper::SQL", strSQL);
        }
        finally
        {
            dbConnect.Close();
        }

        return bRetCode;

    }

    public static bool GetDate(int nRowIndex, String strFieldName, DataTable dt, out DateTime dtResult)
    {
        dtResult = DateTime.Now;
        if (dt.Rows.Count <= 0) return false;
        if (dt.Rows[nRowIndex][strFieldName] == DBNull.Value) return false;

        String strName = dt.Rows[nRowIndex][strFieldName].GetType().Name;

        switch (strName)
        {
            case "DateTime":
                dtResult = Convert.ToDateTime(dt.Rows[nRowIndex][strFieldName]);
                break;

            default:
                return false;
        }

        return true;
    }

    

    public static String GetAbsoluteString(int nRowIndex, int nColIndex, DataTable dt)
    {
        String strResult = "";

        if (dt.Rows.Count <= 0) return strResult;

        if (dt.Rows[nRowIndex][nColIndex] == DBNull.Value) return strResult;

        String strName = dt.Rows[nRowIndex][nColIndex].GetType().Name;

        switch (strName)
        {
            case "Double":
                strResult = Convert.ToDouble(dt.Rows[nRowIndex][nColIndex]).ToString("F2"); //|-- Valeur float avec 2 digits
                break;

            case "Single":
                strResult = Convert.ToSingle(dt.Rows[nRowIndex][nColIndex]).ToString("F2");
                break;

            case "Decimal":
                strResult = Convert.ToDouble(dt.Rows[nRowIndex][nColIndex]).ToString("C2"); //|-- Valeur monetaire avec 2 digits et simbole monetaire
                break;

            case "DateTime":
                strResult = Convert.ToDateTime(dt.Rows[nRowIndex][nColIndex]).ToString("dd/MM/yyyy");
                break;

            default:
                strResult = dt.Rows[nRowIndex][nColIndex].ToString();
                break;
        }

        return strResult;
    }

    public static String GetString(int nRowIndex, String strFieldName, DataTable dt)
    {
        return dt.Rows[nRowIndex][strFieldName].ToString().Trim();
    }

    public static String GetAbsoluteString(int nRowIndex, String strFieldName, DataTable dt)
    {
        String strResult = "";

        if (dt.Rows.Count <= 0) return strResult;
        if (dt.Rows[nRowIndex][strFieldName] == DBNull.Value) return strResult;

        String strName = dt.Rows[nRowIndex][strFieldName].GetType().Name;

        switch (strName)
        {
            case "Double":
                strResult = Convert.ToDouble(dt.Rows[nRowIndex][strFieldName]).ToString("F2"); 
                break;

            case "Single":
                strResult = Convert.ToSingle(dt.Rows[nRowIndex][strFieldName]).ToString("F2"); 
                break;

            case "Decimal":
                strResult = Convert.ToDouble(dt.Rows[nRowIndex][strFieldName]).ToString("C2"); //|-- Valeur monetaire avec 2 digits et simbole monetaire
                break;

            case "DateTime":
                strResult = Convert.ToDateTime(dt.Rows[nRowIndex][strFieldName]).ToString("dd/MM/yyyy");
                break;

            default:
                strResult = dt.Rows[nRowIndex][strFieldName].ToString().Trim();
                break;
        }

        return strResult;
    }

    public static bool GetAbsoluteString(int nRowIndex, String strFieldName, DataTable dt, out String strResult)
    {
        strResult = "";

        if (dt.Rows.Count <= 0) return false;
        if (dt.Rows[nRowIndex][strFieldName] == DBNull.Value) return false;

        String strName = dt.Rows[nRowIndex][strFieldName].GetType().Name;

        switch (strName)
        {
            case "Double":
                strResult = Convert.ToDouble(dt.Rows[nRowIndex][strFieldName]).ToString("C2"); //|-- Valeur monetaire avec 2 digits
                break;

            case "Decimal":
                strResult = Convert.ToDouble(dt.Rows[nRowIndex][strFieldName]).ToString("C2"); //|-- Valeur monetaire avec 2 digits et simbole monetaire
                break;

            case "DateTime":
                strResult = Convert.ToDateTime(dt.Rows[nRowIndex][strFieldName]).ToString("dd/MM/yyyy");
                break;

            default:
                strResult = dt.Rows[nRowIndex][strFieldName].ToString();
                break;
        }

        return true;
    }


    public static short GetShort(int nRowIndex, String strFieldName, DataTable dt)
    {
        short nResult = 0;
        if (dt.Rows.Count <= 0) return nResult;
        if (dt.Rows[nRowIndex][strFieldName] == DBNull.Value) return nResult;
        String strName = dt.Rows[nRowIndex][strFieldName].GetType().Name;
        switch (strName)
        {
            case "Double":
            case "Single":
            case "Byte":
            case "Int32":
            case "Int16":
                nResult = Convert.ToInt16(dt.Rows[nRowIndex][strFieldName]);
                break;

            case "Decimal":
                nResult = Convert.ToInt16(dt.Rows[nRowIndex][strFieldName]);
                break;

            case "String":
                strName = dt.Rows[nRowIndex][strFieldName].ToString();
                if (short.TryParse(strName, out nResult)) return nResult;
                break;

            default:
                break;
        }

        return nResult;
    }

    public static bool GetShort(int nRowIndex, String strFieldName, DataTable dt, ref short nResult)
    {
        nResult = 0;
        if (dt.Rows.Count <= 0) return false;
        if (dt.Rows[nRowIndex][strFieldName] == DBNull.Value) return false;
        String strName = dt.Rows[nRowIndex][strFieldName].GetType().Name;
        switch (strName)
        {
            case "Double":
            case "Single":
            case "Byte":
            case "Int32":
            case "Int16":
                nResult = Convert.ToInt16(dt.Rows[nRowIndex][strFieldName]);
                break;

            case "Decimal":
                nResult = Convert.ToInt16(dt.Rows[nRowIndex][strFieldName]);
                break;

            case "String":
                strName = dt.Rows[nRowIndex][strFieldName].ToString();
                if (short.TryParse(strName, out nResult) == false) return false;
                break;

            default:
                return false;

        }

        return true;
    }

    public static int GetInt(int nRowIndex, String strFieldName, DataTable dt)
    {
        int nResult = 0;
        if (dt.Rows.Count <= 0) return nResult;
        if (dt.Rows[nRowIndex][strFieldName] == DBNull.Value) return nResult;
        String strName = dt.Rows[nRowIndex][strFieldName].GetType().Name;
        switch (strName)
        {
            case "Double":
            case "Single":
            case "Byte":
            case "Int32":
            case "Int16":
                nResult = Convert.ToInt32(dt.Rows[nRowIndex][strFieldName]);
                break;

            case "Decimal":
                nResult = Convert.ToInt32(dt.Rows[nRowIndex][strFieldName]);
                break;

            case "String":
                strName = dt.Rows[nRowIndex][strFieldName].ToString();
                if (int.TryParse(strName, out nResult)) return nResult;
                break;

            default:
                break;
        }

        return nResult;
    }

    //|-- Tient compte de la valeur null
    public static bool GetInt(int nRowIndex, String strFieldName, DataTable dt, ref int nResult)
    {
        nResult = 0;
        if (dt.Rows.Count <= 0) return false;
        if (dt.Rows[nRowIndex][strFieldName] == DBNull.Value) return false;
        String strName = dt.Rows[nRowIndex][strFieldName].GetType().Name;
        switch (strName)
        {
            case "Double":
            case "Single":
            case "Byte":
            case "Int32":
            case "Int16":
                nResult = Convert.ToInt32(dt.Rows[nRowIndex][strFieldName]);
                break;

            case "Decimal":
                nResult = Convert.ToInt32(dt.Rows[nRowIndex][strFieldName]);
                break;

            case "String":
                strName = dt.Rows[nRowIndex][strFieldName].ToString();
                if (int.TryParse(strName, out nResult) == false) return false;
                break;

            default:
                return false;
        }

        return true;
    }

    public static int GetInt(DataRow row, String strColName)
    {
        int nResult = 0;
        if (row[strColName] == DBNull.Value) return nResult;
        String strName = row[strColName].GetType().Name;
        switch (strName)
        {
            case "Double":
            case "Single":
            case "Byte":
            case "Int32":
            case "Int16":
                nResult = Convert.ToInt32(row[strColName]);
                break;

            case "Decimal":
                nResult = Convert.ToInt32(row[strColName]);
                break;

            case "String":
                strName = row[strColName].ToString();
                if (int.TryParse(strName, out nResult)) return nResult;
                break;

            default:
                break;
        }

        return nResult;
    }

    public static long GetLong(int nRowIndex, String strFieldName, DataTable dt)
    {
        long nResult = 0;
        if (dt.Rows.Count <= 0) return nResult;
        if (dt.Rows[nRowIndex][strFieldName] == DBNull.Value) return nResult;
        String strName = dt.Rows[nRowIndex][strFieldName].GetType().Name;
        switch (strName)
        {
            case "Double":
            case "Single":
            case "Byte":
            case "Int64":
            case "Int32":
            case "Int16":
                nResult = Convert.ToInt64(dt.Rows[nRowIndex][strFieldName]);
                break;

            case "Decimal":
                nResult = Convert.ToInt64(dt.Rows[nRowIndex][strFieldName]);
                break;

            case "String":
                strName = dt.Rows[nRowIndex][strFieldName].ToString();
                if (long.TryParse(strName, out nResult)) return nResult;
                break;

            default:
                break;
        }

        return nResult;
    }

    public static bool GetLong(int nRowIndex, String strFieldName, DataTable dt, ref long nResult)
    {
        nResult = 0;
        if (dt.Rows.Count <= 0) return false;
        if (dt.Rows[nRowIndex][strFieldName] == DBNull.Value) return false;
        String strName = dt.Rows[nRowIndex][strFieldName].GetType().Name;
        switch (strName)
        {
            case "Double":
            case "Single":
            case "Byte":
            case "Int64":
            case "Int32":
            case "Int16":
                nResult = Convert.ToInt64(dt.Rows[nRowIndex][strFieldName]);
                break;

            case "Decimal":
                nResult = Convert.ToInt64(dt.Rows[nRowIndex][strFieldName]);
                break;

            case "String":
                strName = dt.Rows[nRowIndex][strFieldName].ToString();
                if (long.TryParse(strName, out nResult) == false) return false;
                break;


            default:
                return false;
        }

        return true;
    }

    public static double GetDouble(int nRowIndex, String strFieldName, DataTable dt)
    {
        double dResult = 0.0;
        if (dt.Rows.Count <= 0) return dResult;
        if (dt.Rows[nRowIndex][strFieldName] == DBNull.Value) return dResult;
        String strName = dt.Rows[nRowIndex][strFieldName].GetType().Name;
        switch (strName)
        {
            case "Double":
            case "Single":
            case "Byte":
            case "Int32":
            case "Int16":
                dResult = Convert.ToDouble(dt.Rows[nRowIndex][strFieldName]);
                break;

            case "Decimal":
                dResult = Convert.ToDouble(dt.Rows[nRowIndex][strFieldName]);
                break;

            case "String":
                strName = dt.Rows[nRowIndex][strFieldName].ToString();
                if (double.TryParse(strName, out dResult)) return dResult;
                break;

            default:
                break;
        }

        return dResult;
    }

    //|-- Tient compte de la valeur null
    public static bool GetDouble(int nRowIndex, String strFieldName, DataTable dt, ref double dResult)
    {
        dResult = 0.0;
        if (dt.Rows.Count <= 0) return false;
        if (dt.Rows[nRowIndex][strFieldName] == DBNull.Value) return false;
        String strName = dt.Rows[nRowIndex][strFieldName].GetType().Name;
        switch (strName)
        {
            case "Double":
            case "Single":
            case "Byte":
            case "Int32":
            case "Int16":
                dResult = Convert.ToDouble(dt.Rows[nRowIndex][strFieldName]);
                break;

            case "Decimal":
                dResult = Convert.ToDouble(dt.Rows[nRowIndex][strFieldName]);
                break;

            case "String":
                strName = dt.Rows[nRowIndex][strFieldName].ToString();
                if (double.TryParse(strName, out dResult) == false) return false;
                break;

            default:
                return false;
        }

        return true;
    }

    public static double GetDouble(DataRow row, String strColName)
    {
        double dResult = 0.0;
        if (row[strColName] == DBNull.Value) return dResult;
        String strName = row[strColName].GetType().Name;
        switch (strName)
        {
            case "Double":
            case "Single":
            case "Byte":
            case "Int32":
            case "Int16":
                dResult = Convert.ToDouble(row[strColName]);
                break;

            case "Decimal":
                dResult = Convert.ToDouble(row[strColName]);
                break;

            case "String":
                strName = row[strColName].ToString();
                if (double.TryParse(strName, out dResult)) return dResult;
                break;

            default:
                break;
        }

        return dResult;
    }

    public static String CharToSQL(String strValue)
    {
        String strResult = "";
        int i;

        for (i = 0; i < strValue.Length; i++)
        {
            strResult += strValue[i];
            if (strValue[i] == '\'')
            {
                strResult += '\'';
            }
        }

        return strResult;
    }

    public static String GetCurrentDate()
    {
        //String strDate = "{d'" + DateTime.Now.Year.ToString("D4") + "-" + DateTime.Now.Month.ToString("D2") + "-" + DateTime.Now.Day.ToString("D2") + "'}";
        return DateToSQL(DateTime.Now, 3);
    }

    public static String DateToSQL(DateTime dt, int nFormat = 1)
    {
        String strResult;
        String strDBType = ConfigurationManager.AppSettings["DBType"];
        switch (nFormat)
        {
            case 1:
                if (strDBType == "Access")
                    strResult = dt.ToString("#yyyy-MM-dd#");
                else
                    strResult = "{d'" + dt.ToString("yyyy-MM-dd") + "'}";
                break;
            case 2:
                if (strDBType == "Access")
                    strResult = dt.ToString("#HH:mm:ss#");
                else
                    strResult = "{t'" + dt.ToString("HH:mm:ss") + "'}";
                break;
            default:    //|-- Date & Time
                if (strDBType == "Access")
                    strResult = dt.ToString("#yyyy-MM-dd HH:mm:ss#");
                else
                    strResult = "{ts'" + dt.ToString("yyyy-MM-dd HH:mm:ss.fff") + "'}";
                break;
        }

        return strResult;
    }

    public static String DateTimeToSQL(DateTime dt, int nFormat = 1)
    {
        String strResult;
        String strDBType = ConfigurationManager.AppSettings["DBType"];
        switch (nFormat)
        {
            case 1:
                if (strDBType == "Access")
                    strResult = dt.ToString("#yyyy-MM-dd#");
                else
                    strResult = "{d'" + dt.ToString("yyyy-MM-dd") + "'}";
                break;
            case 2:
                if (strDBType == "Access")
                    strResult = dt.ToString("#HH:mm:ss#");
                else
                    strResult = "{t'" + dt.ToString("HH:mm:ss") + "'}";
                break;
            default:    //|-- Date & Time
                if (strDBType == "Access")
                    strResult = dt.ToString("#yyyy-MM-dd HH:mm:ss#");
                else
                    strResult = "{ts'" + dt.ToString("yyyy-MM-dd HH:mm:ss.fff") + "'}";
                strResult = strResult.Replace("00:00:00.000", "23:59:59.000");
                break;
        }

        return strResult;
    }
    public static String DateTimeEqualToSQL(DateTime dt, int nFormat = 1)
    {
        String strResult;
        String strDBType = ConfigurationManager.AppSettings["DBType"];
        switch (nFormat)
        {
            case 1:
                if (strDBType == "Access")
                    strResult = dt.ToString("#yyyy-MM-dd#");
                else
                    strResult = "{d'" + dt.ToString("yyyy-MM-dd") + "'}";
                break;
            case 2:
                if (strDBType == "Access")
                    strResult = dt.ToString("#HH:mm:ss#");
                else
                    strResult = "{t'" + dt.ToString("HH:mm:ss") + "'}";
                break;
            default:    //|-- Date & Time
                if (strDBType == "Access")
                    strResult = dt.ToString("#yyyy-MM-dd HH:mm:ss#");
                else
                    strResult = "{ts'" + dt.ToString("yyyy-MM-dd HH:mm:ss.fff") + "'}";
                break;
        }

        return strResult;
    }


     public static String DateTimeEqualToSQL(String strDate, int nFormat = 1)
    {
        String strResult = "NULL";
        DateTime dt;

        if (String.IsNullOrEmpty(strDate)) return strResult;

        try
        {
            dt = DateTime.Parse(strDate);
        }
        catch (Exception ex)
        {
            return strResult;
        }

        return DateTimeEqualToSQL(dt, 3);
    }
    
    public static String DateToSQL(String strDate, int nFormat = 1)
    {
        String strResult = "NULL";
        DateTime dt;

        if (String.IsNullOrEmpty(strDate)) return strResult;

        try
        {
            dt = DateTime.Parse(strDate);
        }
        catch (Exception ex)
        {
            return strResult;
        }

        return DateToSQL(dt, nFormat);
    }

    public static String DateTimeToSQL(String strDate, int nFormat = 1)
    {
        String strResult = "NULL";
        DateTime dt;

        if (String.IsNullOrEmpty(strDate)) return strResult;

        try
        {
            dt = DateTime.Parse(strDate);
        }
        catch (Exception ex)
        {
            return strResult;
        }

        return DateTimeToSQL(dt, 3);
    }

    public static String GetCurrentDateForAccess()
    {
        String strDate = "#" + DateTime.Now.Year.ToString("D4") + "-" + DateTime.Now.Month.ToString("D2") + "-" + DateTime.Now.Day.ToString("D2") + "#";
        return strDate;
    }

    //public static String DateToSQLForAccess(String strDate, int nFormat = 1)
    //{
    //    String strResult = "NULL";
    //    DateTime dt;

    //    if (String.IsNullOrEmpty(strDate)) return strResult;

    //    try
    //    {
    //        dt = DateTime.Parse(strDate);
    //    }
    //    catch
    //    {
    //        return strResult;
    //    }

    //    switch (nFormat)
    //    {
    //        case 1:
    //            strResult = "#" + dt.ToString("yyyy-MM-dd") + "#";
    //            break;
    //        case 2:
    //            strResult = "#" + dt.ToString("hh:mm:ss") + "#";
    //            break;
    //        default:    //|-- Date & Time
    //            strResult = "#" + dt.ToString("yyyy-MM-dd hh:mm:ss") + "#";
    //            //strResult = dt.ToString("{ts'yyyy-MM-dd hh:mm:ss'}");
    //            break;
    //    }
    //    return strResult;
    //}


    //|--------------------------------------------------
    //|-- Pour AC JQuery
    //|---------------------------------------------------
    public static List<object> CreateJSONArray(object[] items)
    {
        List<object> json = new List<object>();

        if (null != items)
            json.AddRange(items);

        return json;
    }


    public static List<object> CreateJSONArray(DataTable table)
    {
        List<object> jsonRows = new List<object>();

        if (null != table)
            foreach (DataRow row in table.Rows)
            {
                SortedDictionary<string, object> jsonRow = new SortedDictionary<string, object>();

                foreach (DataColumn column in table.Columns)
                    jsonRow.Add(column.ColumnName.ToLower(), row[column]);

                jsonRows.Add(jsonRow);
            }

        return CreateJSONArray(jsonRows.ToArray());
    }

}
