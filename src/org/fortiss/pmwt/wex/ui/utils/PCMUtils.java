/***************************************************************************
 * Copyright (c) 2016 the WESSBAS project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package org.fortiss.pmwt.wex.ui.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

import de.uka.ipd.sdq.pcm.repository.ProvidedRole;
import de.uka.ipd.sdq.pcm.system.System;

/**
 * Collection of helper methods to handle PCM models.
 */

public class PCMUtils {
	/**
	 * Helper method that allows reading/writing xmi files without being an
	 * eclipse plugin.
	 */

	public static void registerFactoryForECore() {
		Map<String, Object> mapResource = Resource.Factory.Registry.INSTANCE
				.getExtensionToFactoryMap();
		mapResource.put("*", new XMIResourceFactoryImpl() {
			@Override
			public Resource createResource(URI uri) {
				XMIResource xmiResource = new XMIResourceImpl(uri);
				return xmiResource;
			}
		});
	}

	/**
	 * Retrieves the PCM System Model from a file.
	 * 
	 * @param file
	 *            Source file.
	 * @return An instance of the PCM System Model.
	 * @throws IOException
	 *             Occurs if something unexpected happens.
	 */

	public static System getSystem(File file) throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();

		// -- Register UML: System Model
		Map packageRegistry = resourceSet.getPackageRegistry();
		packageRegistry.put(de.uka.ipd.sdq.pcm.system.SystemPackage.eNS_URI,
				de.uka.ipd.sdq.pcm.system.SystemPackage.eINSTANCE);

		// --
		Resource resource = resourceSet.getResource(
				URI.createFileURI(file.getAbsolutePath()), true);
		resource.load(Collections.EMPTY_MAP);

		System sys = (System) resource.getContents().get(0);
		return sys;
	}

	/**
	 * Returns the provided roles from a PCM System Model.
	 * 
	 * @param sys
	 *            System of interest.
	 * @return An array of provided roles.
	 * @throws IOException
	 *             Occurs if something unexpected happens.
	 */

	public static String[] getProvidedRoleArray(System sys) throws IOException {
		List<String> lstResult = new ArrayList<String>();

		if (sys != null) {
			EList<ProvidedRole> lstProvidedRole = sys
					.getProvidedRoles_InterfaceProvidingEntity();
			for (ProvidedRole providedRole : lstProvidedRole) {
				lstResult.add(providedRole.getEntityName());
			}
		}

		return lstResult.toArray(new String[0]);
	}
}
