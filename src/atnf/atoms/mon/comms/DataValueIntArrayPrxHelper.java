// **********************************************************************
//
// Copyright (c) 2003-2013 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.5.0
//
// <auto-generated>
//
// Generated from file `MoniCA.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package atnf.atoms.mon.comms;

public final class DataValueIntArrayPrxHelper extends Ice.ObjectPrxHelperBase implements DataValueIntArrayPrx
{
    public static DataValueIntArrayPrx checkedCast(Ice.ObjectPrx __obj)
    {
        DataValueIntArrayPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof DataValueIntArrayPrx)
            {
                __d = (DataValueIntArrayPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId()))
                {
                    DataValueIntArrayPrxHelper __h = new DataValueIntArrayPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DataValueIntArrayPrx checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        DataValueIntArrayPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof DataValueIntArrayPrx)
            {
                __d = (DataValueIntArrayPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId(), __ctx))
                {
                    DataValueIntArrayPrxHelper __h = new DataValueIntArrayPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DataValueIntArrayPrx checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DataValueIntArrayPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId()))
                {
                    DataValueIntArrayPrxHelper __h = new DataValueIntArrayPrxHelper();
                    __h.__copyFrom(__bb);
                    __d = __h;
                }
            }
            catch(Ice.FacetNotExistException ex)
            {
            }
        }
        return __d;
    }

    public static DataValueIntArrayPrx checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        DataValueIntArrayPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId(), __ctx))
                {
                    DataValueIntArrayPrxHelper __h = new DataValueIntArrayPrxHelper();
                    __h.__copyFrom(__bb);
                    __d = __h;
                }
            }
            catch(Ice.FacetNotExistException ex)
            {
            }
        }
        return __d;
    }

    public static DataValueIntArrayPrx uncheckedCast(Ice.ObjectPrx __obj)
    {
        DataValueIntArrayPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof DataValueIntArrayPrx)
            {
                __d = (DataValueIntArrayPrx)__obj;
            }
            else
            {
                DataValueIntArrayPrxHelper __h = new DataValueIntArrayPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static DataValueIntArrayPrx uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DataValueIntArrayPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            DataValueIntArrayPrxHelper __h = new DataValueIntArrayPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::atnf::atoms::mon::comms::DataValue",
        "::atnf::atoms::mon::comms::DataValueIntArray"
    };

    public static String ice_staticId()
    {
        return __ids[2];
    }

    protected Ice._ObjectDelM __createDelegateM()
    {
        return new _DataValueIntArrayDelM();
    }

    protected Ice._ObjectDelD __createDelegateD()
    {
        return new _DataValueIntArrayDelD();
    }

    public static void __write(IceInternal.BasicStream __os, DataValueIntArrayPrx v)
    {
        __os.writeProxy(v);
    }

    public static DataValueIntArrayPrx __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            DataValueIntArrayPrxHelper result = new DataValueIntArrayPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }

    public static final long serialVersionUID = 0L;
}
