/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.liaocy.ml4j.exception;

/**
 *
 * @author liaocy
 */
public class NotFoundTermIDException extends Exception {
    public NotFoundTermIDException() {
        super("Not found this term in database");
    }
}
