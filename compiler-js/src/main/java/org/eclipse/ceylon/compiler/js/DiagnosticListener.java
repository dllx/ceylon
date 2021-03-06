/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.js;

import java.io.File;

public interface DiagnosticListener {
    void error(File file, long line, long column, String message);
    void warning(File file, long line, long column, String message);

    // FIXME: special API for default module? Else specify version for default module.
    void moduleCompiled(String module, String version);
}
