package com.google.sps.data;

public final class Comment {
  private final long id;
  private final long timestamp;
  private final String text;

  public Comment(long id, long timestamp, String text) {
    this.id = id;
    this.timestamp = timestamp;
    this.text = text;
  }
}