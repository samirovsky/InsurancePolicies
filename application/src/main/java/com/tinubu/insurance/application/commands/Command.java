package com.tinubu.insurance.application.commands;

public interface Command<T> {
  T aggregateId();
}
