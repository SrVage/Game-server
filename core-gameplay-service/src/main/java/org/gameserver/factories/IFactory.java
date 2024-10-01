package org.gameserver.factories;

public interface IFactory<T, TParam> {
    T create(String firstParam, TParam secondParam);
}
