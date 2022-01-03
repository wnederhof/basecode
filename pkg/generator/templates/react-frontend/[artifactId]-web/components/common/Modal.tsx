import { ReactNode } from 'react'

interface Props {
  title: string
  visible: boolean
  children: ReactNode
}

export function Modal(props: Props) {
  return (
    <>
      {!props.visible ? (
        <></>
      ) : (
        <>
          <h1>{props.title}</h1>
          {props.children}
        </>
      )}
    </>
  )
}
