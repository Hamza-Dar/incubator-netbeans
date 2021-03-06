/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.netbeans.modules.maven.jaxws.nodes;

import java.beans.PropertyChangeEvent;
import java.util.Collections;
import org.netbeans.modules.websvc.project.api.WebService;
import org.netbeans.modules.websvc.project.api.WebServiceData;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import java.beans.PropertyChangeListener;
import org.openide.util.RequestProcessor;

public class JaxWsClientRootChildren extends Children.Keys<WebService> {
    
    private static final RequestProcessor JAX_WS_CLIENT_ROOT_RP =
            new RequestProcessor(JaxWsClientRootChildren.class); //NOI18N
    
    JaxWsListener listener;
    //MavenWebServicesProvider wsDataProvider;
    WebServiceData wsData;

    private final RequestProcessor.Task updateNodeTask = JAX_WS_CLIENT_ROOT_RP.create(new Runnable() {
        @Override
        public void run() {
            updateKeys();
        }
    });
    
    public JaxWsClientRootChildren(WebServiceData wsData) {
        this.wsData = wsData;
    }
    
    @Override
    protected void addNotify() {
        listener = new JaxWsListener();
        wsData.addPropertyChangeListener(listener);
        updateKeys();
    }
    
    @Override
    protected void removeNotify() {
        setKeys(Collections.<WebService>emptySet());
        wsData.removePropertyChangeListener(listener);
    }
       
    private void updateKeys() {
        setKeys(wsData.getServiceConsumers());
    }

    @Override
    protected Node[] createNodes(final WebService key) {
        Node wsNode = key.createNode();
        return wsNode == null ? new Node[0] : new Node[] {wsNode};
    }
    
    class JaxWsListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updateNodeTask.schedule(1000);
        }        
    }
        
}
