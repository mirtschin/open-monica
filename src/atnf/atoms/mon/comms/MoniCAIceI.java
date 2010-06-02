// Copyright (c) 2003-2008 ZeroC, Inc. All rights reserved.
// Copyright (C) 2009 CSIRO Australia Telescope National Facility
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Library General Public License
// as published by the Free Software Foundation; either version 2
// of the License, or (at your option) any later version.

package atnf.atoms.mon.comms;

import java.util.Vector;
import atnf.atoms.mon.*;
import atnf.atoms.time.AbsTime;

/**
 * Concrete implementation of the Ice server for MoniCA.
 * 
 * @author David Brodrick
 */
public final class MoniCAIceI extends _MoniCAIceDisp
{
  /** The currently running server. */
  protected static MoniCAIceServerThread theirServer = null;

  public MoniCAIceI()
  {
  }

  /** Add the new points to the system. */
  public boolean addPoints(PointDescriptionIce[] newpoints, String encname, String encpass, Ice.Current __current)
  {
    // Decrypt the user's credentials
    String username = KeyKeeper.decrypt(encname);
    String password = KeyKeeper.decrypt(encpass);
    // TODO: Currently does nothing
    return false;
  }

  /** Get the names of all points on the system (including aliases). */
  public String[] getAllPointNames(Ice.Current __current)
  {
    return PointDescription.getAllPointNames();
  }

  /** Get all unique points on the system. */
  public PointDescriptionIce[] getAllPoints(Ice.Current __current)
  {
    // Get all unique points
    PointDescription[] points = PointDescription.getAllUniquePoints();
    return MoniCAIceUtil.getPointDescriptionsAsIce(points);
  }

  /** Return the definitions for the specified points. */
  public PointDescriptionIce[] getPoints(String[] names, Ice.Current __current)
  {
    PointDescriptionIce[] temp = new PointDescriptionIce[names.length];
    int numfound = 0;
    for (int i = 0; i < names.length; i++) {
      PointDescription thispoint = PointDescription.getPoint(names[i]);
      if (thispoint != null) {
        temp[i] = MoniCAIceUtil.getPointDescriptionAsIce(thispoint);
        numfound++;
      }
    }
    PointDescriptionIce[] res;
    if (numfound == names.length) {
      res = temp;
    } else {
      res = new PointDescriptionIce[numfound];
      int nextres = 0;
      for (int i = 0; i < names.length; i++) {
        if (temp[i] != null) {
          res[nextres] = temp[i];
          nextres++;
        }
      }
    }
    return res;
  }

  /** Add the new setup to the system. */
  public boolean addSetup(String setup, String encname, String encpass, Ice.Current __current)
  {
    // Decrypt the user's credentials
    String username = KeyKeeper.decrypt(encname);
    String password = KeyKeeper.decrypt(encpass);
    // TODO: Currently does nothing
    return false;
  }

  /** Return the string representation of all saved setups stored on the server. */
  public String[] getAllSetups(Ice.Current __current)
  {
    SavedSetup[] allsetups = SavedSetup.getAllSetups();
    String[] stringsetups = new String[allsetups.length];
    for (int i = 0; i < allsetups.length; i++) {
      stringsetups[i] = allsetups[i].toString();
    }
    return stringsetups;
  }

  /** Return historical data for the specified points. */
  public PointDataIce[][] getArchiveData(String[] names, long start, long end, long maxsamples, Ice.Current __current)
  {
    AbsTime absstart = AbsTime.factory(start);
    AbsTime absend = AbsTime.factory(end);
    PointDataIce[][] res = new PointDataIce[names.length][];
    for (int i = 0; i < names.length; i++) {
      Vector<PointData> thisdata = PointBuffer.getPointData(names[i], absstart, absend, (int) maxsamples);
      if (thisdata == null) {
        thisdata = new Vector<PointData>(0);
      }
      res[i] = MoniCAIceUtil.getPointDataAsIce(thisdata);
    }
    return res;
  }

  /** Return the latest values for the given points. */
  public PointDataIce[] getData(String[] names, Ice.Current __current)
  {
    PointDataIce[] temp = new PointDataIce[names.length];
    for (int i = 0; i < names.length; i++) {
      PointData pd = PointBuffer.getPointData(names[i]);
      PointDataIce pdi;
      if (pd != null) {
        pdi = MoniCAIceUtil.getPointDataAsIce(PointBuffer.getPointData(names[i]));
      } else {
        // No data available so create dummy data with null value
        pdi = MoniCAIceUtil.getPointDataAsIce(new PointData(names[i]));
      }
      temp[i] = pdi;
    }
    return temp;
  }

  /** Set new values for the specified points. */
  public boolean setData(String[] names, PointDataIce[] rawvalues, String encname, String encpass, Ice.Current __current)
  {
    if (names.length != rawvalues.length) {
      return false;
    }
    int numpoints = names.length;

    // Decrypt the user's credentials
    // / Doesn't presently do anything with user's credentials
    String username = KeyKeeper.decrypt(encname);
    String password = KeyKeeper.decrypt(encpass);

    // Process each of the control requests consecutively
    boolean result = true;
    Vector<PointData> values = MoniCAIceUtil.getPointDataFromIce(rawvalues);
    for (int i = 0; i < numpoints; i++) {
      try {
        // Get the specified point
        PointDescription thispoint = PointDescription.getPoint(names[i]);
        if (thispoint == null) {
          MonitorMap.logger.warning("MoniCAIceI.setData: Point " + names[i] + " does not exist");
          System.err.println("MoniCAIceI.setData: Point " + names[i] + " does not exist");
          result = false;
          continue;
        }
        // Act on the new data value
        thispoint.firePointEvent(new PointEvent(this, values.get(i), true));
      } catch (Exception e) {
        MonitorMap.logger.warning("MoniCAIceI.setData: Processing " + names[i] + ": " + e);
        System.err.println("MoniCAIceI.setData: Processing " + names[i] + ": " + e);
        result = false;
      }
    }

    return result;
  }

  /** Return the key and modulus required to send encrypted data to the server. */
  public String[] getEncryptionInfo(Ice.Current __current)
  {
    String[] a = new String[2];
    a[0] = KeyKeeper.getPublicKey();
    a[1] = KeyKeeper.getModulus();
    return a;
  }

  /** Return the current time on the server. */
  public long getCurrentTime(Ice.Current __current)
  {
    return (new AbsTime()).getValue();
  }

  /** Start the server on the default port. */
  public static void startIceServer()
  {
    startIceServer(MoniCAIceUtil.getDefaultPort());
  }

  /** Start the server using the specified adapter. */
  public static void startIceServer(Ice.ObjectAdapter a)
  {
    if (theirServer != null) {
      theirServer.shutdown();
    }
    theirServer = new MoniCAIceServerThreadAdapter(a);
    theirServer.start();
  }

  /** Start the server using the specified port number. */
  public static void startIceServer(int port)
  {
    if (theirServer != null) {
      theirServer.shutdown();
    }
    theirServer = new MoniCAIceServerThreadPort(port);
    theirServer.start();
  }

  /** Shutdown the currently running Ice server. */
  public static void stopIceServer()
  {
    theirServer.shutdown();
    theirServer = null;
  }

  /** Start a new thread to run the server using an existing adapter. */
  public abstract static class MoniCAIceServerThread extends Thread
  {
    /** The name of the service which is registered with Ice. */
    protected static String theirServiceName = "MoniCAService";

    /** The adapter to start the server with. */
    protected Ice.ObjectAdapter itsAdapter = null;

    public MoniCAIceServerThread()
    {
      super("MoniCAIceServer");
    }

    /** Start the server and block until it is registered. */
    public void start()
    {
      super.start();
      // Block until service is registered
      while (itsAdapter == null || itsAdapter.find(itsAdapter.getCommunicator().stringToIdentity(theirServiceName)) == null) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
        }
      }
    }

    /** Stop the server and block until it is deregistered. */
    public void shutdown()
    {
      itsAdapter.remove(itsAdapter.getCommunicator().stringToIdentity(theirServiceName));
      // Block until service is unregistered
      while (itsAdapter.find(itsAdapter.getCommunicator().stringToIdentity(theirServiceName)) != null) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
        }
      }
    }
  };

  /** Start a new thread to run the server using an existing adapter. */
  public static class MoniCAIceServerThreadAdapter extends MoniCAIceServerThread
  {
    public MoniCAIceServerThreadAdapter(Ice.ObjectAdapter a)
    {
      super();
      itsAdapter = a;
    }

    public void run()
    {
      Ice.Communicator ic = null;
      try {
        ic = itsAdapter.getCommunicator();
        Ice.Object object = new MoniCAIceI();
        itsAdapter.add(object, ic.stringToIdentity(theirServiceName));
        ic.waitForShutdown();
      } catch (Ice.LocalException e) {
        e.printStackTrace();
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
      if (ic != null) {
        try {
          ic.destroy();
        } catch (Exception e) {
          System.err.println(e.getMessage());
        }
      }
    }
  };

  /** Start a new thread to run the server using a new adapter on specified port. */
  public static class MoniCAIceServerThreadPort extends MoniCAIceServerThread
  {
    /**
     * The port to start the server on. Not used if a adapter has been
     * explicitly specified.
     */
    protected int itsPort;

    public MoniCAIceServerThreadPort(int port)
    {
      super();
      itsPort = port;
    }

    /** Stop the server and block until it is deregistered. */
    public void shutdown()
    {
      super.shutdown();
      itsAdapter.deactivate();
    }

    public void run()
    {
      Ice.Communicator ic = null;
      try {
        // Need to create a new adapter
        ic = Ice.Util.initialize();
        itsAdapter = ic.createObjectAdapterWithEndpoints("MoniCAIceAdapter", "tcp -p " + itsPort);
        Ice.Object object = new MoniCAIceI();
        itsAdapter.add(object, ic.stringToIdentity(theirServiceName));
        itsAdapter.activate();
        ic.waitForShutdown();
      } catch (Ice.LocalException e) {
        e.printStackTrace();
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
      if (ic != null) {
        try {
          ic.destroy();
        } catch (Exception e) {
          System.err.println(e.getMessage());
        }
      }
    }
  };

}
