// Copyright 2018 FantasyRank. All rights reserved.
package org.flatcode.common;

import com.google.common.base.Strings;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author wgrim
 * @since 2018-10-14
 */
public class MorePreconditions {

  public static String checkNotNullOrEmpty(@Nullable String value) {
    return checkNotNullOrEmpty(value, null);
  }

  public static String checkNotNullOrEmpty(@Nullable String value, @Nullable String message) {
    checkState(!Strings.isNullOrEmpty(value), message);
    return value;
  }
}
