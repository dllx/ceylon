/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
import org.eclipse.ceylon.ceylondoc.test.modules.dependency.b { b, B }
import org.eclipse.ceylon.ceylondoc.test.modules.dependency.b.bb { b2, B2 }
import org.eclipse.ceylon.ceylondoc.test.modules.dependency.c { C }

shared B fceB() { throw; }
shared C fceC() { throw; }

"External links:
 
 - b = [[b]]
 - B = [[B]]
 - B.b = [[B.b]]
 - b2 = [[b2]]
 - B2 = [[B2]]
 - B2.b2 = [[B2.b2]]

 - org.eclipse.ceylon.ceylondoc.test.modules.dependency.b::b = [[org.eclipse.ceylon.ceylondoc.test.modules.dependency.b::b]]
 - org.eclipse.ceylon.ceylondoc.test.modules.dependency.b::B = [[org.eclipse.ceylon.ceylondoc.test.modules.dependency.b::B]]
 - org.eclipse.ceylon.ceylondoc.test.modules.dependency.b::B.b = [[org.eclipse.ceylon.ceylondoc.test.modules.dependency.b::B.b]]
 - org.eclipse.ceylon.ceylondoc.test.modules.dependency.b.bb::b2 = [[org.eclipse.ceylon.ceylondoc.test.modules.dependency.b.bb::b2]]
 - org.eclipse.ceylon.ceylondoc.test.modules.dependency.b.bb::B2 = [[org.eclipse.ceylon.ceylondoc.test.modules.dependency.b.bb::B2]]
 - org.eclipse.ceylon.ceylondoc.test.modules.dependency.b.bb::B2.b2 = [[org.eclipse.ceylon.ceylondoc.test.modules.dependency.b.bb::B2.b2]]
 
 "     
shared void fceWithExternalLinksInDoc() {}