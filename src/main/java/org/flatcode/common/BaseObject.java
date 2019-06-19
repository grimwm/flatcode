// Copyright 2018 FantasyRank. All rights reserved.
package org.flatcode.common;

import com.google.common.base.Functions;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;

import java.util.Arrays;
import java.util.Objects;

/**
 * Implements hashCode/equals functionality via significant attributes and provides a {@link Functions.ToStringFunction}
 * for pretty-printing.
 *
 * @author wgrim
 * @since 2018-10-14
 */
public abstract class BaseObject {

  @Override
  public int hashCode() {
    return Objects.hash(significantAttributes());
  }

  @Override
  public boolean equals(Object obj) {
    if (Objects.isNull(obj)) {
      return false;
    } else if (obj == this) {
      return true;
    } else if (!getClass().isAssignableFrom(obj.getClass())) {
      return false;
    }
    return Arrays.equals(significantAttributes(), getClass().cast(obj).significantAttributes());
  }

  /**
   * All objects returned from here from {@code this} should contribute to determining an object's hash code and equality.
   * A suggestion for handling circular references back to {@code this} should be handled by way of a unique identifier
   * in the other's {@link #significantAttributes()} instead of the object itself.
   * @return An array of all the {@link Object} important for hashCode/equality.
   */
  protected abstract Object[] significantAttributes();

  protected ToStringHelper toStringHelper() {
    return MoreObjects.toStringHelper(this);
  }

  @Override
  public String toString() {
    return toStringHelper().toString();
  }
}
