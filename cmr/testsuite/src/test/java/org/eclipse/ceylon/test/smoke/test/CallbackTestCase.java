/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.test.smoke.test;

import java.io.File;
import java.net.URL;

import org.eclipse.ceylon.cmr.api.ArtifactCallback;
import org.eclipse.ceylon.cmr.api.ArtifactCallbackStream;
import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.api.CmrRepository;
import org.eclipse.ceylon.cmr.api.ModuleQuery;
import org.eclipse.ceylon.cmr.api.ModuleSearchResult;
import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.cmr.api.RepositoryManagerBuilder;
import org.eclipse.ceylon.cmr.impl.DefaultRepository;
import org.eclipse.ceylon.cmr.impl.RemoteContentStore;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class CallbackTestCase extends AbstractTest {
    private String name = "org.eclipse.fizbiz";
    private String version = "1.0.0.Beta1";

    @Test
    public void testDefault() throws Exception {
        TestArtifactCallback callback = new TestArtifactCallback();
        ArtifactContext context = new ArtifactContext(null, name, version);
        context.setCallback(callback);
        doTest(context, callback);
    }

    @Test
    public void testThreadLocal() throws Exception {
        TestArtifactCallback callback = new TestArtifactCallback();
        ArtifactCallbackStream.setCallback(callback);
        try {
            ArtifactContext context = new ArtifactContext(null, name, version);
            doTest(context, callback);
        } finally {
            ArtifactCallbackStream.setCallback(null);
        }
    }

    @Test
    public void testErr() throws Exception {
        TestArtifactCallback callback = new TestArtifactCallback();
        callback.ts = String.valueOf(System.currentTimeMillis());
        ArtifactContext context = new ArtifactContext(null, name, version);
        context.setCallback(callback);
        doTest(context, callback);
    }

    protected void doTest(ArtifactContext context, TestArtifactCallback callback) throws Exception {
        String repoURL = "http://jboss-as7-modules-repository.googlecode.com/svn/trunk/ceylon";
        try {
            new URL(repoURL).openStream();
        } catch (Exception e) {
            log.error(String.format("Cannot connect to repo %s - %s", repoURL, e));
            return; // probably not on the internet?
        }

        RepositoryManagerBuilder builder = getRepositoryManagerBuilder(false, 60000, java.net.Proxy.NO_PROXY);
        RemoteContentStore rcs = new RemoteContentStore(repoURL, log, false, 60000, java.net.Proxy.NO_PROXY);
        CmrRepository repo = new DefaultRepository(rcs.createRoot());
        RepositoryManager manager = builder.addRepository(repo).buildRepository();

        try {
            File file = manager.getArtifact(context);
            Assert.assertNotNull(file);
            Assert.assertTrue(callback.size > 0);
            Assert.assertEquals(callback.size, callback.current);
        } catch (Exception re) {
            Assert.assertNotNull(callback.ts);
            Assert.assertNotNull(callback.err);
            Assert.assertEquals(callback.ts, callback.err.getMessage());
        } finally {
            manager.removeArtifact(null, name, version);
            // test if remove really works
            testSearchResults("org.eclipse.fizbiz", ModuleQuery.Type.JVM, new ModuleSearchResult.ModuleDetails[0]);
        }
    }

    private static class TestArtifactCallback implements ArtifactCallback {
        private String ts;

        private long size;
        private long current;
        private Throwable err;

        public void start(String nodeFullPath, long size, String contentStore) {
            if (ts != null) {
                throw new IllegalArgumentException(ts);
            }
            this.size = size;
        }

        public void read(byte[] bytes, int length) {
            current += length;
        }

        public void done(File file) {
            Assert.assertNotNull(file);
        }

        public void error(File file, Throwable err) {
            this.err = err;
        }
    }
}
