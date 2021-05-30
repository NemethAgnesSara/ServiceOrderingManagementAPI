package com.example.serviceordering;

public enum ServiceOrderStateType {
    acknowledged,
    rejected,
    pending,
    held,
    inProgress,
    cancelled,
    completed,
    failed,
    partial,
    assessingCancellation,
    pendingCancellation
}
