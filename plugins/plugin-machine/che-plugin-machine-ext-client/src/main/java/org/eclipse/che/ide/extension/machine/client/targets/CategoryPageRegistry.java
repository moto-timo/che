/*******************************************************************************
 * Copyright (c) 2012-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.extension.machine.client.targets;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.commons.annotation.Nullable;
import org.eclipse.che.ide.util.loging.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Registry for pages.
 *
 * @author Oleksii Orel
 */
@Singleton
public class CategoryPageRegistry {

    private final Map<String, CategoryPage> categoryPageByCategoryMap;

    public CategoryPageRegistry() {
        this.categoryPageByCategoryMap = new HashMap<>();
    }

    @Inject(optional = true)
    private void register(Set<CategoryPage> categoryPages) {
        for (CategoryPage page : categoryPages) {
            final String category = page.getCategory();
            if (this.categoryPageByCategoryMap.containsKey(category)) {
                Log.warn(this.getClass(), "Category page with category '" + category + "' is already registered.");
            } else {
                this.categoryPageByCategoryMap.put(category, page);
            }
        }
    }

    /**
     * Returns CategoryPage with the specified Category or {@code null} if none.
     *
     * @param targetCategory
     *         the type of the target
     * @return target or {@code null}
     */
    @Nullable
    public CategoryPage getCategoryPageByCategory(String targetCategory) {
        return categoryPageByCategoryMap.get(targetCategory);
    }
}
